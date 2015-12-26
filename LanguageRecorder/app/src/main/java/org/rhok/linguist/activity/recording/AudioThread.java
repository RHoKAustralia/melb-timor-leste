package org.rhok.linguist.activity.recording;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.rhok.linguist.code.DiskSpace;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Manages audio recording and playback. Each audio thread
 * records to a single file, therefore each call to
 * {@link #startRecording} overwrites any previous recording.
 *
 * Thread safety: All public methods are thread safe. Uses
 * message passing internally to ensure all audio operations
 * occur on the correct thread.
 *
 * Only a single instance of AudioThread can exist at any point
 * in time. This prevents contention over audio resources, however
 * doesn't prevent you from trying to use an AudioThread that's been
 * released somewhere else...
 */
public class AudioThread extends HandlerThread {

    private static final String TAG = "AudioThread";

    private static final int MSG_START_RECORDING = 1;
    private static final int MSG_STOP_RECORDING = 2;
    private static final int MSG_PLAY_RECORDING = 3;
    private static final int MSG_STOP_PLAYING = 4;
    private static final int MSG_PLAY_FILE = 5;
    private static final int MSG_RELEASE = 6;

    private static AudioThread instance;
    private MessageHandler mHandler;
    private final String audioFilename =
            UUID.randomUUID().toString().replaceAll("-", "").concat(".mp4");

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private PlaybackCompletionListener mPlaybackCompletionListener;

    public interface PlaybackCompletionListener {
        void onPlaybackComplete();
    }

    private static class MessageHandler extends Handler {
        private WeakReference<AudioThread> mAudioThread;

        public MessageHandler(AudioThread audioThread, Looper looper) {
            super(looper);
            mAudioThread = new WeakReference<>(audioThread);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "msg: " + msg.what);

            switch (msg.what) {
                case MSG_START_RECORDING:
                    mAudioThread.get().startRecording();
                    break;
                case MSG_STOP_RECORDING:
                    mAudioThread.get().stopRecording();
                    break;
                case MSG_PLAY_RECORDING:
                    mAudioThread.get().playRecording();
                    break;
                case MSG_PLAY_FILE:
                    mAudioThread.get().playFile(msg.getData().getString("path"), msg.arg1 == 1);
                    break;
                case MSG_STOP_PLAYING:
                    mAudioThread.get().stopPlaying();
                    break;
                case MSG_RELEASE:
                    mAudioThread.get().release();
                    break;
                default:
                    Log.e(TAG, "Unknown message: " + msg.what);
                    break;
            }
        }
    }

    /**
     * Create a new AudioThread. Blocks until thread is ready.
     */
    private AudioThread() {
        super(TAG);
        start();
        mHandler = new MessageHandler(this, getLooper());
    }

    /** Get the single AudioThread instance, creating one if necessary */
    public static AudioThread getInstance() {
        if (instance == null) {
            instance = new AudioThread();
        }
        return instance;
    }

    /** Get the file path that audio is recorded to */
    public String getAudioFilename() {
        return audioFilename;
    }

    /** Stop recording audio */
    public void stopRecording() {
        if (!isMyThread()) {
            sendMessage(MSG_STOP_RECORDING);
        } else {
            if (mRecorder != null) {
                try {
                    mRecorder.stop();
                } catch (RuntimeException e) {
                    Log.e(TAG, "Error during stopRecording()", e);
                } finally {
                    mRecorder.release();
                    mRecorder = null;
                }
            }
        }
    }

    /**
     * Start recording audio to file. Overwrites audio recorded by
     * any previous calls to {@link #startRecording}.
     */
    public void startRecording()
    {
        if (!isMyThread()) {
            sendMessage(MSG_START_RECORDING);
        } else {
            if (mRecorder != null) {
                Log.w(TAG, "startRecording called while recording");
                stopRecording();
            }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setOutputFile(DiskSpace.getAudioFileBasePath() + audioFilename);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(TAG, "Error during mRecorder.prepare()", e);
            }

            Log.i(TAG, "recording started for filename: " + audioFilename);

            mRecorder.start();
        }

    }

    /** Play the most recently recorded audio, if any exists */
    public void playRecording()
    {
        if (!isMyThread()) {
            sendMessage(MSG_PLAY_RECORDING);
        } else {
            stopPlaying();
            mPlayer = new MediaPlayer();
            mPlayer.setLooping(true);
            try {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        //playRecording();
                    }
                });
                mPlayer.setDataSource(DiskSpace.getAudioFileBasePath() + audioFilename);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e(TAG, "Error during playRecording()", e);
            }
        }
    }

    /** Set action to perform on playback completion */
    public void setPlaybackCompletionListener(PlaybackCompletionListener listener) {
        mPlaybackCompletionListener = listener;
    }

    /** Play the specified audio file */
    public void playFile(String path) {
        playFile(path, false);
    }

    /** Play the specified audio file, optionally looping */
    public void playFile(String path, boolean loop) {
        if (!isMyThread()) {
            int loopInt = loop ? 1 : 0;
            Message msg = mHandler.obtainMessage(MSG_PLAY_FILE, loopInt, 0);
            Bundle data = new Bundle();
            data.putString("path", path);
            msg.setData(data);
            mHandler.sendMessage(msg);
        } else {
            Log.d(TAG, "playFile(): " + path);
            stopPlaying();
            mPlayer = new MediaPlayer();
            mPlayer.setLooping(loop);
            try {
                if (mPlaybackCompletionListener != null) {
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mPlaybackCompletionListener.onPlaybackComplete();
                        }
                    });
                }
                mPlayer.setDataSource(path);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e(TAG, "Error during playFile()", e);
            }
        }
    }

    /** Stop playing audio and release player */
    public void stopPlaying() {
        if (!isMyThread()) {
            sendMessage(MSG_STOP_PLAYING);
        } else {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    try {
                        mPlayer.stop();
                    } catch (IllegalStateException e) {
                        Log.e(TAG, "mPlayer.stop() failed", e);
                    }
                }
                mPlayer.release();
                mPlayer = null;
            }
        }
    }

    /** Release audio resources & gracefully close thread */
    public void release() {
        if (!isMyThread()) {
            sendMessage(MSG_RELEASE);
        } else {
            Log.d(TAG, "release()");
            stopRecording();
            stopPlaying();
            quit();
            instance = null;
        }
    }

    /** Returns true if the current thread is this AudioThread's thread. */
    private boolean isMyThread() {
        return Thread.currentThread().getId() == this.getId();
    }

    /** Send an integer message to the audio thread's handler. */
    private void sendMessage(int msg) {
        mHandler.sendMessage(mHandler.obtainMessage(msg));
    }
}
