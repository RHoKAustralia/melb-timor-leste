package org.rhok.linguist.activity.recording;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
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

                if (msg.obj.toString().equals("startrecording")) {
                    startRecording();
                }
                if (msg.obj.toString().equals("stoprecording")) {
                    stopRecording();
                    startPlaying();
                }
                if (msg.obj.toString().equals("startplaying")) {
                    startPlaying();
                }
                if (msg.obj.toString().equals("stopplaying")) {
                    stopPlaying();
                }
                if (msg.obj.toString().equals("release")) {
                    release();
                }

            }
        };

        Looper.loop();
    }

    /** Stop recording audio and release recorder */
    private void stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
            } catch (RuntimeException e) {
                // TODO (Warwick): any cleanup required here?
                Log.e(TAG, "Error during stopRecording()", e);
            }
            mRecorder.release();
            mRecorder = null;
        }
    }

    private void startRecording()
    {
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

    private void startPlaying()
    {
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
        }
        catch (IOException e) {
            Log.e(TAG, "Error during startPlaying()", e);
        }
    }

    /** Stop playing audio and release player */
    private void stopPlaying() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void release() {
        Log.d(TAG, "release()");
        stopRecording();
        stopPlaying();
        if (Looper.myLooper() != null) {
            Looper.myLooper().quit();
        }
    }
}