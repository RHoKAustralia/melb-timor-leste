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
import java.util.UUID;

/**
 * Created by lachlan on 3/07/2015.
 */

public class AudioThread extends Thread {

    private static final String TAG = "AudioThread";

    private static final String MSG_START_RECORDING = "startrecording";
    private static final String MSG_STOP_RECORDING = "stoprecording";
    private static final String MSG_START_PLAYING = "startplaying";
    private static final String MSG_STOP_PLAYING = "stopplaying";
    private static final String MSG_PLAY_FILE = "playfile";
    private static final String MSG_RELEASE = "release";

    public Handler mHandler;
    public String audioFilename;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    @Override
    public void run() {
        Log.d(TAG, "run()");
        Looper.prepare();

        mHandler = new Handler() {
            public void handleMessage(Message msg) {

                Log.i(TAG, msg.obj.toString());

                if (msg.obj.toString().equals(MSG_START_RECORDING)) {
                    startRecording();
                }
                if (msg.obj.toString().equals(MSG_STOP_RECORDING)) {
                    stopRecording();
                    startPlaying();
                }
                if (msg.obj.toString().equals(MSG_START_PLAYING)) {
                    startPlaying();
                }
                if (msg.obj.toString().equals(MSG_PLAY_FILE)) {
                    playFile(msg.getData().getString("path"));
                }
                if (msg.obj.toString().equals(MSG_STOP_PLAYING)) {
                    stopPlaying();
                }
                if (msg.obj.toString().equals(MSG_RELEASE)) {
                    release();
                }

            }
        };

        Looper.loop();
    }

    /** Stop recording audio and release recorder */
    public void stopRecording() {
        if (Thread.currentThread().getId() != this.getId()) {
            Message msg = mHandler.obtainMessage();
            msg.obj = MSG_STOP_RECORDING;
            mHandler.sendMessage(msg);
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
            } else {
                Log.w(TAG, "stopRecording called when mRecorder == null");
            }
        }
    }

    public void startRecording()
    {
        if (Thread.currentThread().getId() != this.getId()) {
            Message msg = mHandler.obtainMessage();
            msg.obj = MSG_START_RECORDING;
            mHandler.sendMessage(msg);
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
            Message msg = mHandler.obtainMessage();
            msg.obj = MSG_START_PLAYING;
            mHandler.sendMessage(msg);
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
            Message msg = mHandler.obtainMessage();
            msg.obj = MSG_PLAY_FILE;
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
            Message msg = mHandler.obtainMessage();
            msg.obj = MSG_STOP_PLAYING;
            mHandler.sendMessage(msg);
        } else {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                mPlayer.release();
                mPlayer = null;
            }
        }
    }

    public void release() {
        if (Thread.currentThread().getId() != this.getId()) {
            Message msg = mHandler.obtainMessage();
            msg.obj = MSG_RELEASE;
            mHandler.sendMessage(msg);
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
