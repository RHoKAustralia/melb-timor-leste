package org.rhok.linguist.activity.recording;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;
import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.util.StringUtils;

public class RecordingInstructionsActivity extends BaseInterviewActivity {

    private int personId;
    private Interview interview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_instructions);

        personId = getIntent().getIntExtra(IntentUtil.ARG_PERSON_ID, -1);
        Study study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);
        setTitle(study.getName());
        if( !StringUtils.isNullOrEmpty(study.getInstructions())){
            ((TextView)findViewById(R.id.questionText)).setText(study.getInstructions());
        }
        interview = new Interview(study);
        interview.set__intervieweeid(personId);

    }

    public void nextButtonClick(android.view.View view) {
        Study study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);

        Intent intent = new Intent(this, RecordingFragmentActivity.class);
        intent.putExtra(IntentUtil.ARG_INTERVIEW, interview);
        intent.putExtra(IntentUtil.ARG_STUDY, study);
        startActivity(intent);

    }
}