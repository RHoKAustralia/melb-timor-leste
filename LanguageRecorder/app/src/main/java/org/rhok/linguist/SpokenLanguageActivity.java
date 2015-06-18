package org.rhok.linguist;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.Person;
import org.rhok.linguist.interview.BaseInterviewActivity;
import org.rhok.linguist.interview.InterviewLivedLifeActivity;
import org.rhok.linguist.interview.InterviewMoreLanguagesActivity;


public class SpokenLanguageActivity extends BaseInterviewActivity {

    private String selectedLanguage = "";
    private String nextActivity = "";
    private int languageNumber;
    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoken_language);
        setTitle("Select Language");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView question = (TextView)findViewById(R.id.language_question);
        question.setText(languageQuestion());
        populateLanguages();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nextActivity = extras.getString("NEXT_ACTIVITY");
            _person = (Person) extras.getSerializable("Person");
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }




    public void nextButtonClick(android.view.View view) {

        if (nextActivity.equals("Study")) {
            Intent intent = new Intent(this, StudyActivity.class);
            intent.putExtra("LANGUAGE", selectedLanguage);
            startActivity(intent);
        }
        if (nextActivity.equals("MoreLanguages")) {

            Intent intent = new Intent(this, InterviewMoreLanguagesActivity.class);

            languageNumber = getIntent().getExtras().getInt("LanguageNumber");
            if (languageNumber == 1) {
                _person.firstlanguage = selectedLanguage;
            }
            if (languageNumber == 2) {
                _person.secondlanguage = selectedLanguage;
            }
            if (languageNumber == 3) {
                _person.thirdlanguage = selectedLanguage;
            }
            if (languageNumber == 4) {
                _person.otherlanguages = selectedLanguage;

                intent = new Intent(this, InterviewLivedLifeActivity.class);
            }
            intent.putExtra("Person", _person);
            intent.putExtra("LastLanguageNumber", languageNumber);
            startActivity(intent);
        }
    }

    private String languageQuestion() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("LANGUAGE_QUESTION");
        } else {
            //TODO when pressing the UP button on the "select study" activity,
            // the intent is null, so the "extras" is  null. Needs to be fixed somehow
        }
        return value;
    }

    private void populateLanguages() {

        ListViewPopulator.populate(this, R.id.language_list, R.array.study_languages, true,
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedLanguage = (String) parent.getItemAtPosition(position);
                    //Log.i("languageapp", selectedLanguage);
                    view.setSelected(true);
                }
            }
        );

    }


}
