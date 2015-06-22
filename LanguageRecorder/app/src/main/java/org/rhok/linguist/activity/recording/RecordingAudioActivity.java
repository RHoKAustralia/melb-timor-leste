package org.rhok.linguist.activity.recording;

import android.content.Intent;
import android.os.Bundle;
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

    private int pictureCount = 1;
    private boolean transcribing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_audio);

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

    }

    public void nextButtonClick(android.view.View view) {

        if (!transcribing) {
            recordingQuestionTextView.setVisibility(View.GONE);
            recordingMessageTextView.clearAnimation();
            recordingMessageTextView.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);

            recordOkTextView.setVisibility(View.VISIBLE);

            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);

        } else {

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
    }

    public void yesButtonClick(View view) {

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
