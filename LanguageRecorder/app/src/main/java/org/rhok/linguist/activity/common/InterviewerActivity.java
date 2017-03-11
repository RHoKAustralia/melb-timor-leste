package org.rhok.linguist.activity.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.rhok.linguist.R;
import org.rhok.linguist.api.models.Interviewer;
import org.rhok.linguist.code.PreferencesHelper;

/**
 * Create / edit the single interviewer instance
 * Created by bramleyt on 11/03/2017.
 */
public class InterviewerActivity extends AppCompatActivity{
    private AQuery aq;
    private Interviewer interviewer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interviewer);
        aq = new AQuery(this);

        aq.id(R.id.emailEditText).getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    nextButtonClick(v);
                    return true;
                }
                return false;
            }
        });

        if(savedInstanceState!=null){
            interviewer = (Interviewer) savedInstanceState.getSerializable("interviewer");
        }
        if(interviewer==null)
            interviewer = PreferencesHelper.getInterviewer();
        if(interviewer==null)
            interviewer = PreferencesHelper.createDefaultInterviewer();
        modelToUI();
    }

    private void modelToUI(){
        aq.id(R.id.nameEditText).text(interviewer.getName());
        aq.id(R.id.mobileEditText).text(interviewer.getMobile());
        aq.id(R.id.emailEditText).text(interviewer.getEmail());

    }
    public void nextButtonClick(View v){
        interviewer.setName(aq.id(R.id.nameEditText).getText().toString().trim());
        interviewer.setMobile(aq.id(R.id.mobileEditText).getText().toString().trim());
        interviewer.setEmail(aq.id(R.id.emailEditText).getText().toString().trim());
        PreferencesHelper.saveInterviewer(interviewer);
        setResult(RESULT_OK);
        Toast.makeText(this, "Interviewer Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

}
