package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.activity.location.InterviewMunicipalityActivity;

public class InterviewMoreLanguagesActivity extends BaseInterviewActivity {

    private Person _person;
    private int _lastLanguageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_more_languages);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable(IntentUtil.ARG_PERSON);
        _lastLanguageNumber = extras.getInt("LastLanguageNumber");
    }

    public void noButtonClick(View view)
    {
        Intent intent = new Intent(this, InterviewMunicipalityActivity.class);
        intent.putExtra(IntentUtil.ARG_PERSON, _person);
        intent.putExtra("mode", "lives");
        intent.putExtra(InterviewNameActivity.ARG_FINAL_INTENT,
                getIntent().getParcelableExtra(InterviewNameActivity.ARG_FINAL_INTENT));
        startActivity(intent);
    }

    public void yesButtonClick(View view)
    {
        Intent intent = new Intent(this, InterviewSpokenLanguageActivity.class);

        if (_lastLanguageNumber == 1) {
            intent.putExtra("LANGUAGE_QUESTION", "(secondary language?)");
        }
        if (_lastLanguageNumber == 2) {
            intent.putExtra("LANGUAGE_QUESTION", "(third language?)");
        }
        if (_lastLanguageNumber == 3) {
            intent.putExtra("LANGUAGE_QUESTION", "(fourth language?)");
        }

        intent.putExtra("LanguageNumber", _lastLanguageNumber +1);
        intent.putExtra(IntentUtil.ARG_PERSON, _person);
        intent.putExtra("NEXT_ACTIVITY", "MoreLanguages");
        intent.putExtra(InterviewNameActivity.ARG_FINAL_INTENT,
                getIntent().getParcelableExtra(InterviewNameActivity.ARG_FINAL_INTENT));
        startActivity(intent);

    }
}
