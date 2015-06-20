package org.rhok.linguist.interview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.rhok.linguist.R;
import org.rhok.linguist.SpokenLanguageActivity;
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.Person;

import java.util.ArrayList;

public class InterviewEducationActivity extends ActionBarActivity {

    private Person _person;
    private String selectedEducation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_education);

        Intent intent = getIntent();
        _person = (Person) intent.getSerializableExtra("person");

        populateEducations();

        //setTitle("Interview - Education");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview, menu);
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


    private void populateEducations() {

        ListViewPopulator.populate(this, R.id.educationListView, R.array.education, false,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedEducation = (String) parent.getItemAtPosition(position);
                    }
                });
    }

    public void nextButtonClick(android.view.View view) {

        if (selectedEducation == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select an occupation", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            _person.education = selectedEducation;

            String question = getResources().getString(R.string.interview_primarylanguage);
            Intent intent = new Intent(this, SpokenLanguageActivity.class);
            intent.putExtra("LANGUAGE_QUESTION", question);
            intent.putExtra("NEXT_ACTIVITY", "MoreLanguages");
            intent.putExtra("Person", _person);
            intent.putExtra("LanguageNumber", 1);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}