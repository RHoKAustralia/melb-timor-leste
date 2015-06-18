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
import org.rhok.linguist.SpokenLanguageActivity;
import org.rhok.linguist.code.Person;

public class InterviewNameActivity extends BaseInterviewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_name);
    }

    public void nextButtonClick(android.view.View view) {

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
