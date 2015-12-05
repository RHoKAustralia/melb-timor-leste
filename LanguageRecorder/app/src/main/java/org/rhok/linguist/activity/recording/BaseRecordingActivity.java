package org.rhok.linguist.activity.recording;

import android.content.Intent;

import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.interview.BaseInterviewActivity;
import org.rhok.linguist.api.models.Study;

/**
 * Created by bramleyt on 5/12/2015.
 */
public abstract class BaseRecordingActivity extends BaseInterviewActivity {

    public static final String ARG_RECORD_INDEX = "rhok.recordIndex";
    public Study getStudy(){
        return (Study) getIntent().getSerializableExtra(IntentUtil.ARG_STUDY);
    }

    protected void navigateNextQuestion(){

    }
}
