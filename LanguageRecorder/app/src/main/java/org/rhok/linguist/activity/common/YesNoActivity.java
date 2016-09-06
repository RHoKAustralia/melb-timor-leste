package org.rhok.linguist.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.location.InterviewMunicipalityActivity;
import org.rhok.linguist.application.LinguistApplication;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.util.StringUtils;

/**
 * Use with
 * startActivityForResult
 */
public class YesNoActivity extends AppCompatActivity {


    public static final String ARG_ACTIVITY_TITLE = "activity.title";
    public static final String ARG_QUESTION_TEXT = "questionText";

    public static final int RESULT_YES = 1;
    public static final int RESULT_NO = 2;

    private static final int MAX_TITLE_LENGTH = 22;

    public static Intent makeYesNoIntent(String title, String question){
        Intent intent = new Intent(LinguistApplication.getContextStatic(), YesNoActivity.class);
        intent.putExtra(ARG_ACTIVITY_TITLE, title);
        intent.putExtra(ARG_QUESTION_TEXT, question);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yesno);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString(ARG_ACTIVITY_TITLE);
        if(!StringUtils.isNullOrEmpty(title)) {
            if(title.length()>MAX_TITLE_LENGTH && getSupportActionBar()!=null){
                getSupportActionBar().setSubtitle(title);
            }
            else setTitle(title);
        }
        TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setText(extras.getString(ARG_QUESTION_TEXT));

    }



    public void onYesNoClicked(View view) {
        Intent resultData = new Intent();
        resultData.putExtra(IntentUtil.ARG_NEXT_ACTIVITY_ARGS, getIntent().getBundleExtra(IntentUtil.ARG_NEXT_ACTIVITY_ARGS));
        resultData.putExtra(IntentUtil.ARG_NEXT_ACTIVITY_CLASS, getIntent().getStringExtra(IntentUtil.ARG_NEXT_ACTIVITY_CLASS));
        setResult(view.getId() == R.id.btn_yes ? RESULT_YES : RESULT_NO, resultData);
        finish();
    }
}
