package org.rhok.linguist.activity.recording;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.rhok.linguist.code.DiskSpace;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Created by lachlan on 3/07/2015.
 *
 * FIXME: it's possible to cause a NPE by creating this object
 * then calling a public method before the message handler has
 * been created.
 */

public class AudioThread extends Thread {

    private static final String TAG = "AudioThread";

    private static final int MSG_START_RECORDING = 1;
    private static final int MSG_STOP_RECORDING = 2;
    private static final int MSG_START_PLAYING = 3;
    private static final int MSG_STOP_PLAYING = 4;
    private static final int MSG_PLAY_FILE = 5;
    private static final int MSG_RELEASE = 6;

    private MessageHandler mHandler;
    // TODO: make this private
    public String audioFilename;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private static class MessageHandler extends Handler {
        private WeakReference<AudioThread> mAudioThread;

        public MessageHandler(AudioThread audioThread) {
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
                    // TODO: call this from client code
                    mAudioThread.get().startPlaying();
                    break;
                case MSG_START_PLAYING:
                    mAudioThread.get().startPlaying();
                    break;
                case MSG_PLAY_FILE:
                    mAudioThread.get().playFile(msg.getData().getString("path"));
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

    @Override
    public void run() {
        Log.d(TAG, "run()");
        Looper.prepare();
        mHandler = new MessageHandler(this);
        Looper.loop();
    }

    /** Stop recording audio and release recorder */
    public void stopRecording() {
        if (Thread.currentThread().getId() != this.getId()) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_STOP_RECORDING));
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

    public void startRecording()
    {
        if (Thread.currentThread().getId() != this.getId()) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_START_RECORDING));
        } else {
            if (mRecorder != null) {
                Log.w(TAG, "startRecording called while recording");
                stopRecording();
            }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            if (audioFilename == null) {
                audioFilename = UUID.randomUUID().toString().replaceAll("-", "").concat(".mp4");
            }

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

    public void startPlaying()
    {
        if (Thread.currentThread().getId() != this.getId()) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_START_PLAYING));
        } else {
            mPlayer = new MediaPlayer();
            mPlayer.setLooping(true);
            try {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        //startPlaying();
                    }
                });
                mPlayer.setDataSource(DiskSpace.getAudioFileBasePath() + audioFilename);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e(TAG, "Error during startPlaying()", e);
            }
        }
    }

    public void playFile(String path) {
        if (Thread.currentThread().getId() != this.getId()) {
            Message msg = mHandler.obtainMessage(MSG_PLAY_FILE);
            Bundle data = new Bundle();
            data.putString("path", path);
            msg.setData(data);
            mHandler.sendMessage(msg);
        } else {
            Log.d(TAG, "playFile(): " + path);
            stopPlaying();
            mPlayer = new MediaPlayer();
            mPlayer.setLooping(true);
            try {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer arg0) {
                        //startPlaying();
                    }
                });
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
        if (Thread.currentThread().getId() != this.getId()) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_STOP_PLAYING));
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

    public void release() {
        if (Thread.currentThread().getId() != this.getId()) {
            mHandler.sendMessage(mHandler.obtainMessage(MSG_RELEASE));
        } else {
            Log.d(TAG, "release()");
            stopRecording();
            stopPlaying();
            if (Looper.myLooper() != null) {
                Looper.myLooper().quit();
            }
        }
    }
}
