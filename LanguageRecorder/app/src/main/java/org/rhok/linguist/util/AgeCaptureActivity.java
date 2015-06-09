package org.rhok.linguist.util;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.rhok.linguist.HomeActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.Person;

public class AgeCaptureActivity extends ActionBarActivity {

    private int age, personId;
    private EditText ageEditText;
    private boolean editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_capture);
        populatePersonDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_age_capture, menu);
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

    private void populatePersonDetails() {
        Intent intent = getIntent();
        personId = intent.getIntExtra(HomeActivity.INTENT_PERSONID, -1);
        String windowTitle = getResources().getString(R.string.person_title);

        if (personId != -1) {
            editMode = true;
            windowTitle = getResources().getString(R.string.person_title_edit);

            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            Person person = db.getPerson(personId);
            if (person != null) {
                ageEditText.setText(person.name);
            }
        }
    }
}
