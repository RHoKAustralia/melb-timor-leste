package org.rhok.linguist.interview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.rhok.linguist.location.MunicipalityActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.code.Person;
import org.rhok.linguist.recording.RecordingInstructionsActivity;

public class InterviewLivedLifeActivity extends ActionBarActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_lived_life);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable("Person");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview_lived_life, menu);
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

    public void noButtonClick(View view) {

        Intent intent = new Intent(this, InterviewLivedLengthActivity.class);
        intent.putExtra("Person", _person);
        startActivity(intent);
    }

    public void yesButtonClick(View view) {
        _person.livesinyears = null;

        Intent intent = new Intent(this, RecordingInstructionsActivity.class);
        intent.putExtra("Person", _person);
        intent.putExtra("from", "born");
        startActivity(intent);
    }
}
