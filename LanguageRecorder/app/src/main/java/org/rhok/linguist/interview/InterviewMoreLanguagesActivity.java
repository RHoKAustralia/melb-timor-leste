package org.rhok.linguist.interview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.rhok.linguist.R;
import org.rhok.linguist.SpokenLanguageActivity;
import org.rhok.linguist.code.Person;
import org.rhok.linguist.location.MunicipalityActivity;

public class InterviewMoreLanguagesActivity extends BaseInterviewActivity {

    private Person _person;
    private int _lastLanguageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_more_languages);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable("Person");
        _lastLanguageNumber = extras.getInt("LastLanguageNumber");
    }

    public void noButtonClick(View view)
    {
        Intent intent = new Intent(this, MunicipalityActivity.class);
        intent.putExtra("Person", _person);
        startActivity(intent);
    }

    public void yesButtonClick(View view)
    {
        Intent intent = new Intent(this, SpokenLanguageActivity.class);

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
        intent.putExtra("Person", _person);
        intent.putExtra("NEXT_ACTIVITY", "MoreLanguages");
        startActivity(intent);

    }
}
