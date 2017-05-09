package org.rhok.linguist.activity.recording;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;
import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.util.StringUtils;

// removed in preference of prompt text per phrase - Warwick
@Deprecated
public class RecordingInstructionsActivity extends BaseInterviewActivity {

    private static final String TAG = "RecInstructActivity";

    private Interview interview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_recording_instructions);

        Study study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);
        setTitle(study.getName());
        if( !StringUtils.isNullOrEmpty(study.getInstructions())){
            ((TextView)findViewById(R.id.questionText)).setText(study.getInstructions());
        }
        interview = (Interview) getIntent().getSerializableExtra(IntentUtil.ARG_INTERVIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void nextButtonClick(android.view.View view) {
        Study study = (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);

        Intent intent = new Intent(this, RecordingFragmentActivity.class);
        intent.putExtra(IntentUtil.ARG_INTERVIEW, interview);
        intent.putExtra(IntentUtil.ARG_STUDY, study);
        startActivity(intent);

    }
}
