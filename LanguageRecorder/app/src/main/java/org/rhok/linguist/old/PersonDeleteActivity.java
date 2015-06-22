package org.rhok.linguist.old;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.rhok.linguist.R;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.code.Person;
import org.rhok.linguist.code.PersonWord;
import org.rhok.linguist.old.HomeActivity;
import org.rhok.linguist.old.PersonActivity;

import java.io.File;

public class PersonDeleteActivity extends ActionBarActivity {

    public final static String INTENT_PERSONDELETED = "org.rhok.linguist.persondeleted";

    private TextView personNameTextView;
    private int personId;
    private String personName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_delete);

        personNameTextView = (TextView) findViewById(R.id.person_name_textview);
        Intent intent = getIntent();
        personId = intent.getIntExtra(PersonActivity.INTENT_PERSONID, -1);

        if (personId != -1) {
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            Person person = db.getPerson(personId);
            if (person != null) {

                PersonWord[] words = db.getWordsForPerson(personId);

                personNameTextView.setText(person.name + ", " + words.length + " items captured");

                personName = person.name;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public void deletePerson(android.view.View view) {

        deleteAudioFiles(personId);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        db.deletePerson(personId);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(INTENT_PERSONDELETED, personName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    private void deleteAudioFiles(int PersonID) {

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        PersonWord[] words = db.getWordsForPerson(PersonID);

        for (int i=0;i<words.length;i++) {
            String audioFilename = words[i].audiofilename;

            if (audioFilename != null) {
                if (audioFilename.length() > 0) {
                    String basePath = DiskSpace.getAudioFileBasePath();
                    File f = new File(basePath + audioFilename);

                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
        }

    }


}