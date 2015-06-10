package org.rhok.linguist;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.rhok.linguist.interview.InterviewNameActivity;


public class HomeNewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_new, menu);
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

    public void loadLanguageActivity(android.view.View view) {
        Intent intent = new Intent(this, SpokenLanguageActivity.class);
        intent.putExtra("LANGUAGE_QUESTION", "Which language do you want to research?");
        startActivity(intent);
    }

    public void startInterviewButton(android.view.View view) {

        Intent intent = new Intent(this, InterviewNameActivity.class);
        startActivity(intent);
    }
}