package org.rhok.linguist.activity.recording;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.util.StringUtils;

public class RecordingInstructionsActivity extends BaseRecordingActivity {

    private int personId;
    private Interview interview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_instructions);

        personId = getIntent().getIntExtra(IntentUtil.ARG_PERSON_ID, -1);
        Study study = getStudy();
        if( !StringUtils.isNullOrEmpty(study.getInstructions())){
            ((TextView)findViewById(R.id.questionText)).setText(study.getInstructions());
        }
        interview = new Interview(study);

    }

    public void nextButtonClick(android.view.View view) {

        Intent intent = new Intent(this, RecordingAudioLocalActivity.class);
        intent.putExtra(IntentUtil.ARG_PERSON_ID, personId);
        intent.putExtra(IntentUtil.ARG_STUDY, personId);
        startActivity(intent);

    }
}