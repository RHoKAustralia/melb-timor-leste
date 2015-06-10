package org.rhok.linguist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.rhok.linguist.code.ListViewPopulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class SpokenLanguageActivity extends ActionBarActivity {
    private String selectedLanguage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoken_language);
        setTitle("Select Language");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView question = (TextView)findViewById(R.id.language_question);
        question.setText(languageQuestion());
        populateLanguages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spoken_language, menu);
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

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }




    public void loadStudyActivity(android.view.View view) {
        Intent intent = new Intent(this, StudyActivity.class);
        intent.putExtra("LANGUAGE", selectedLanguage);
        startActivity(intent);
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
