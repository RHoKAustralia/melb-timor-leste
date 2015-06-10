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
import org.rhok.linguist.code.ListViewPopulator;
import org.rhok.linguist.code.Person;

import java.util.ArrayList;

public class InterviewOccupationActivity extends ActionBarActivity {

    private Person _person;
    private String selectedOccupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_occupation);

        Intent intent = getIntent();
        _person = (Person) intent.getSerializableExtra("person");

        populateOccupations();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview_occupation, menu);
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


    private void populateOccupations() {

        ListViewPopulator.populate(this, R.id.occupationListView, R.array.occupations, true,
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOccupation = (String) parent.getItemAtPosition(position);
            }
        });
    }

    public void continueButtonClick(android.view.View view) {

        if (selectedOccupation == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select an occupation", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            _person.occupation = selectedOccupation;

            Intent intent = new Intent(this, InterviewEducationActivity.class);
            intent.putExtra("person", _person);
            startActivity(intent);
        }

    }

}
