package com.example.lachlan.myfirstapp.interview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lachlan.myfirstapp.R;
import com.example.lachlan.myfirstapp.code.Person;

public class InterviewAgeActivity extends ActionBarActivity {

    private Person _person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_age);

        Intent intent = getIntent();
        _person = (Person) intent.getSerializableExtra("person");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview_age, menu);
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

    public void continueButtonClick(android.view.View view) {

        EditText editText = (EditText)findViewById(R.id.ageEditText);
        String age = editText.getText().toString();

        if (age.trim().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter an age", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            _person.age = Integer.parseInt(age);

//            Intent intent = new Intent(this, InterviewAgeActivity.class);
 //           intent.putExtra("person", p);
  //          startActivity(intent);
        }

    }
}
