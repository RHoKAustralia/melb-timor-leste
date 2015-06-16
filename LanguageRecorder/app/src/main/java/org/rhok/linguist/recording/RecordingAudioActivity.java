package org.rhok.linguist.recording;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.rhok.linguist.HomeNewActivity;
import org.rhok.linguist.R;

public class RecordingAudioActivity extends ActionBarActivity {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recording_audio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int pictureCount = 1;

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
                Intent intent = new Intent(this, HomeNewActivity.class);
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

        transcribing = true;
    }
}
