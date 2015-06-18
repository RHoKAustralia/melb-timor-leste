package org.rhok.linguist.interview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.rhok.linguist.R;
import org.rhok.linguist.SplashActivity;
import org.rhok.linguist.code.Person;

public class InterviewAgeActivity extends BaseInterviewActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_age);

        Intent intent = getIntent();
        _person = (Person) intent.getSerializableExtra("person");
    }


    public void nextButtonClick(android.view.View view) {

        EditText editText = (EditText)findViewById(R.id.ageEditText);
        String age = editText.getText().toString();

        if (age.trim().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter an age", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            _person.age = Integer.parseInt(age);

            Intent intent = new Intent(this, InterviewGenderActivity.class);
            intent.putExtra("person", _person);
            startActivity(intent);
        }

    }
}
