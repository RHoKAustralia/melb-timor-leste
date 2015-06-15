package org.rhok.linguist.interview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.rhok.linguist.location.MunicipalityActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.code.Person;

public class InterviewLivedLengthActivity extends ActionBarActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_lived_length);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable("Person");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview_lived_length, menu);
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

    public void nextButtonClick(View view) {
        EditText editText = (EditText)findViewById(R.id.longLivedEditText);
        String longLived = editText.getText().toString();

        if (longLived.trim().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter number of years", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            _person.livesinyears = Integer.parseInt(longLived);

            Intent intent = new Intent(this, MunicipalityActivity.class);
            intent.putExtra("person", _person);
            intent.putExtra("from", "born");
            startActivity(intent);
        }
    }

}
