package org.rhok.linguist.recording;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.rhok.linguist.R;
import org.rhok.linguist.SplashActivity;
import org.rhok.linguist.interview.BaseInterviewActivity;

public class RecordingTranscribeActivity extends BaseInterviewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_transcribe);

        ImageView imageView = (ImageView)findViewById(R.id.captureImageView);
        imageView.setImageResource(R.drawable.goat);

    }


    public void nextButtonClick(android.view.View view) {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

}
