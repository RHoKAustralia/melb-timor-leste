package org.rhok.linguist.activity.recording;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.common.SplashActivity;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;
import org.rhok.linguist.code.DatabaseHelper;

public class RecordingAudioActivity extends BaseInterviewActivity {

    TextView recordingQuestionTextView;
    TextView recordingMessageTextView;
    TextView recordOkTextView;
    TextView transcribeTextView;
    Button nextButton;
    Button yesButton;
    Button noButton;
    Animation anim;
    EditText transcribeEditText;
    ImageView imageView;

    private int personId;
    private int pictureCount = 1;
    private int totalPictures = 10;
    private boolean transcribing = false;
    private boolean playing = false;

    AudioThread audioThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_audio);

        personId = getIntent().getIntExtra(IntentUtil.ARG_PERSON_ID, -1);

        imageView = (ImageView)findViewById(R.id.captureImageView);
        imageView.setImageResource(R.drawable.word1);

        recordingQuestionTextView = (TextView) findViewById(R.id.recordingQuestionTextView );
        recordingMessageTextView  = (TextView) findViewById(R.id.recordingMessageTextView );
        recordOkTextView = (TextView) findViewById(R.id.recordOkTextView);
        transcribeTextView = (TextView) findViewById(R.id.transcribeTextView);
        transcribeEditText = (EditText) findViewById(R.id.transcribeEditText);

        nextButton = (Button) findViewById(R.id.nextRecordingButton);
        yesButton = (Button) findViewById(R.id.yesButton);
        noButton = (Button) findViewById(R.id.noButton);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        recordingMessageTextView.startAnimation(anim);

        audioThread = new AudioThread();
        audioThread.start();

//        audioThread.mHandler.sendMessage(createMessage("startrecording"));

    }

    private Message createMessage(String text) {
        Message msg = Message.obtain();
        msg.obj = text;
        return msg;
    }


    private void startRecording()
    {
        audioThread.mHandler.sendMessage(createMessage("startrecording"));
    }
    private void stopRecording()
    {
        audioThread.mHandler.sendMessage(createMessage("stoprecording"));
        playing = true;
    }
    private void startPlaying()
    {
        audioThread.mHandler.sendMessage(createMessage("startplaying"));
    }
    private void stopPlaying()
    {
        audioThread.mHandler.sendMessage(createMessage("stopplaying"));
        playing = false;
    }

    private boolean playingIsPaused = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (playing) {
            stopPlaying();
            playingIsPaused = true;
        }
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (playingIsPaused) {
            startPlaying();
            playingIsPaused = false;
        }
        // The activity has become visible (it is now "resumed").
    }


    public void nextButtonClick(android.view.View view) {

        if (!transcribing) {

            stopRecording();
            //startPlaying();

            recordingQuestionTextView.setVisibility(View.GONE);
            recordingMessageTextView.clearAnimation();
            recordingMessageTextView.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);

            recordOkTextView.setVisibility(View.VISIBLE);

            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);

        } else {


            String transcribedWord = transcribeEditText.getText().toString();

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            dbHelper.saveWord(personId, pictureCount, transcribedWord, audioThread.audioFilename);

            transcribing = false;
            pictureCount++;

            if (pictureCount > totalPictures) {
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
            }
            else {
                transcribeEditText.setVisibility(View.GONE);
                transcribeTextView.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);

                recordingQuestionTextView.setVisibility(View.VISIBLE);
                recordingMessageTextView.setVisibility(View.VISIBLE);
                recordingMessageTextView.startAnimation(anim);

                //TODO - there's got to be a better way to do this
                if (pictureCount == 2) {
                    imageView.setImageResource(R.drawable.word2);
                }
                if (pictureCount == 3) {
                    imageView.setImageResource(R.drawable.word3);
                }
                if (pictureCount == 4) {
                    imageView.setImageResource(R.drawable.word4);
                }
                if (pictureCount == 5) {
                    imageView.setImageResource(R.drawable.word5);
                }
                if (pictureCount == 6) {
                    imageView.setImageResource(R.drawable.word6);
                }
                if (pictureCount == 7) {
                    imageView.setImageResource(R.drawable.word7);
                }
                if (pictureCount == 8) {
                    imageView.setImageResource(R.drawable.word8);
                }
                if (pictureCount == 9) {
                    imageView.setImageResource(R.drawable.word9);
                }
                if (pictureCount == 10) {
                    imageView.setImageResource(R.drawable.word10);
                }

                audioThread.audioFilename = null;
                startRecording();
            }
        }

    }

    public void noButtonClick(View view) {
        recordingQuestionTextView.setVisibility(View.VISIBLE);
        recordingMessageTextView.startAnimation(anim);
        recordingMessageTextView.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        recordOkTextView.setVisibility(View.GONE);

        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);

        stopPlaying();
        startRecording();
    }

    public void yesButtonClick(View view) {

        stopPlaying();

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.saveWord(personId, pictureCount, null, audioThread.audioFilename);

        recordOkTextView.setVisibility(View.GONE);

        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);

        transcribeEditText.setText("");
        transcribeEditText.setVisibility(View.VISIBLE);
        transcribeTextView.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);

        if (transcribeEditText.requestFocus()) {

           InputMethodManager mgr = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
           mgr.showSoftInput(transcribeEditText, InputMethodManager.SHOW_IMPLICIT);
        }

        transcribing = true;
    }
}
