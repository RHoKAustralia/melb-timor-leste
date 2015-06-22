package org.rhok.linguist.activity.recording;

import android.content.Intent;
import android.os.Bundle;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;

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