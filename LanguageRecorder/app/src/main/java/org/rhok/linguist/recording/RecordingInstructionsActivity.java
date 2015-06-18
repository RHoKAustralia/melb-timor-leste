package org.rhok.linguist.recording;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.rhok.linguist.R;
import org.rhok.linguist.interview.BaseInterviewActivity;
import org.rhok.linguist.interview.InterviewOccupationActivity;

public class RecordingInstructionsActivity extends BaseInterviewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_instructions);
    }

    public void nextButtonClick(android.view.View view) {

        Intent intent = new Intent(this, RecordingAudioActivity.class);
        startActivity(intent);

    }
}