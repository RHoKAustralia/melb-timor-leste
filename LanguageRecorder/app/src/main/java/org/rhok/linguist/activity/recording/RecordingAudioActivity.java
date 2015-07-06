package org.rhok.linguist.activity.recording;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.rhok.linguist.R;
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
    private boolean transcribing = false;

    AudioThread audioThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_audio);

        personId = getIntent().getIntExtra("PersonId", -1);

        imageView = (ImageView)findViewById(R.id.captureImageView);
        imageView.setImageResource(R.drawable.goat);

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
    }
    private void startPlaying()
    {
        audioThread.mHandler.sendMessage(createMessage("startplaying"));
    }
    private void stopPlaying()
    {
        audioThread.mHandler.sendMessage(createMessage("stopplaying"));
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

            if (pictureCount == 4) {
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

                if (pictureCount == 2) {
                    imageView.setImageResource(R.drawable.church);
                }
                if (pictureCount == 3) {
                    imageView.setImageResource(R.drawable.rooster);
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
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        transcribing = true;
    }
}
