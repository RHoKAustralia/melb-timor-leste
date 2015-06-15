package org.rhok.linguist;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.location.MunicipalityActivity;


public class StudyActivity extends ActionBarActivity {
    private String selectedStudy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        setTitle("Select study for " + selectedLanguage());
        populateStudies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_study, menu);
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
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    public void nextButtonClick(android.view.View view) {
        Intent intent = new Intent(this, MunicipalityActivity.class);
        intent.putExtra("STUDY", selectedStudy);
        startActivity(intent);
    }

    private void populateStudies() {

        ListViewPopulator.populate(this, R.id.study_list, R.array.dummy_studies, true,
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedStudy = (String) parent.getItemAtPosition(position);
                }
            }
        );


    }



    private String selectedLanguage() {
        Bundle extras = getIntent().getExtras();
        String value = "";
        if (extras != null) {
            value = extras.getString("LANGUAGE");
        }
        return value;
    }
}
