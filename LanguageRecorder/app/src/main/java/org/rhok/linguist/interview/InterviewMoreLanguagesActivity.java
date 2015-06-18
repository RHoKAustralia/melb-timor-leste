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

public class InterviewMoreLanguagesActivity extends ActionBarActivity {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview_more_languages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
