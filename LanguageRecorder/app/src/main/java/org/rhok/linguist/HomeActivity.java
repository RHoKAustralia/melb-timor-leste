package org.rhok.linguist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.Person;
import org.rhok.linguist.util.NameCaptureActivity;


public class HomeActivity extends ActionBarActivity {

    public final static String INTENT_PERSONID = "org.rhok.linguist.personid";

    Spinner selectPersonSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        selectPersonSpinner = (Spinner) findViewById(R.id.person_spinner);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Person[] people = db.getPeople();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, people);
        selectPersonSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        String message = intent.getStringExtra(PersonActivity.INTENT_PERSONSAVED);

        if (message != null) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();

            int personid = intent.getIntExtra(PersonActivity.INTENT_PERSONSAVEDID, 0);

            //TODO select the right person in the selectPersonSpinner based on personid

            for (int i=0;i<=people.length;i++) {
                if (people[i].personid == personid) {
                    selectPersonSpinner.setSelection(i);
                    break;
                }
            }
        } else {

            String personDeletedName = intent.getStringExtra(PersonDeleteActivity.INTENT_PERSONDELETED);

            if (personDeletedName != null) {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, personDeletedName + " has been deleted", Toast.LENGTH_SHORT);
                toast.show();
            }
            else {

                // restore the last person selected
                if (savedInstanceState != null) {
                    int selectedItem = savedInstanceState.getInt("selectedperson");
                    selectPersonSpinner.setSelection(selectedItem);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        int selectedItem = selectPersonSpinner.getSelectedItemPosition();
        outState.putInt("selectedperson", selectedItem);
    }

    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            int selectedItem = savedInstanceState.getInt("selectedperson");
            selectPersonSpinner.setSelection(selectedItem);
        }
    }

    public void startButton(android.view.View view) {
        Person p = (Person)selectPersonSpinner.getSelectedItem();
        if (p != null) {
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.putExtra(INTENT_PERSONID, p.personid);
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            String windowTitle = getResources().getString(R.string.home_select_person_toast);

            Toast toast = Toast.makeText(context, R.string.home_select_person_toast, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void newPersonButton(android.view.View view) {
        Intent intent = new Intent(this, NameCaptureActivity.class);
        startActivity(intent);
    }

    public void editPersonButton(android.view.View view) {
        Person p = (Person)selectPersonSpinner.getSelectedItem();
        if (p != null) {
            Intent intent = new Intent(this, NameCaptureActivity.class);
            intent.putExtra(INTENT_PERSONID, p.personid);
            startActivity(intent);
        }
    }

    public void uploadButton(android.view.View view) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);

    }
}