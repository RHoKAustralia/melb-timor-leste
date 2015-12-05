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
import org.rhok.linguist.code.DatabaseHelper;

/**
 * Record audio/text for Record objects, using fragments and remote images
 */
public class RecordingFragmentActivity extends RecordingInstructionsActivity {

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

    public void onRecordingAudioFinished(int phraseIndex, String audioFilename){

    }
}
