package com.example.lachlan.myfirstapp.interview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lachlan.myfirstapp.R;
import com.example.lachlan.myfirstapp.SpokenLanguageActivity;
import com.example.lachlan.myfirstapp.code.Person;

public class InterviewNameActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview_name, menu);
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

        EditText editText = (EditText)findViewById(R.id.nameEditText);
        String name = editText.getText().toString();

        if (name.trim().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT);
            toast.show();
        } else {

            Intent intent = new Intent(this, InterviewAgeActivity.class);
            Person p = new Person();
            p.name = name;

            intent.putExtra("person", p);
            startActivity(intent);
        }

    }
}
