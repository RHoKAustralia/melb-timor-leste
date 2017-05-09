package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.recording.InterviewResponseLanguageActivity;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.entity.Person;

public class InterviewLivedLifeActivity extends BaseInterviewActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_lived_life);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable(IntentUtil.ARG_PERSON);

        String question =
                getResources()
                        .getString(R.string.interview_lived_life)
                        .replace("##village##", _person.livesInVillage);

        TextView livedLifeTextView = (TextView) findViewById(R.id.interview_lived_life);
        livedLifeTextView.setText(question);
    }

    public void noButtonClick(View view) {

        _person.livedWholeLife = false;

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.updatePersonLivedWholeLife(_person.personid, false);

        Intent intent = new Intent(this, InterviewLivedLengthActivity.class);
        intent.putExtra(IntentUtil.ARG_PERSON, _person);
        intent.putExtra(InterviewNameActivity.ARG_FINAL_INTENT,
                getIntent().getParcelableExtra(InterviewNameActivity.ARG_FINAL_INTENT));
        startActivity(intent);
    }

    public void yesButtonClick(View view) {
        _person.livedWholeLife = true;
        _person.livedInYears = null;

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.updatePersonLivedWholeLife(_person.personid, true);

        Intent intent = getIntent().getParcelableExtra(InterviewNameActivity.ARG_FINAL_INTENT);
        if(intent==null) intent=new Intent(this, InterviewResponseLanguageActivity.class);
        intent.putExtra(IntentUtil.ARG_PERSON_ID, _person.personid);

        startActivity(intent);
    }
}
