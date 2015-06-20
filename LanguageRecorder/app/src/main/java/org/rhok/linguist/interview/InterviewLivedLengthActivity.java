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

public class InterviewLivedLengthActivity extends BaseInterviewActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_lived_length);

        Bundle extras = getIntent().getExtras();
        _person = (Person) extras.getSerializable("Person");

        //setTitle("Interview - Lived");
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
