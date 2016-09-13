package org.rhok.linguist.activity.common;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.IntentUtil;
import org.rhok.linguist.activity.interview.InterviewNameActivity;
import org.rhok.linguist.activity.old.HomeActivity;
import org.rhok.linguist.activity.old.UploadActivity;
import org.rhok.linguist.activity.recording.InterviewResponseLanguageActivity;
import org.rhok.linguist.activity.recording.RecordingInstructionsActivity;
import org.rhok.linguist.application.LinguistApplication;
import org.rhok.linguist.code.LocaleHelper;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        if(LinguistApplication.DEBUG){
            menu.add(0, R.id.menu_old_home,0, "Old Home");
            menu.add(0, R.id.menu_person_list,0, "Person list");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent=null;
        switch (id) {
            case R.id.action_settings:
                intent = new Intent(this, AppSettingsActivity.class);
                break;

            case R.id.action_upload:
                intent = new Intent(this, UploadInterviewsActivity.class);
                break;
            case R.id.menu_old_home:
                intent = new Intent(this, HomeActivity.class);
                break;
            case R.id.menu_person_list:
                intent = new Intent(this, PersonListActivity.class);
        }
        if(intent!=null){
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // If the user changes the locale in the settings we need to
        // recreate the activity so that the new resources are loaded in
        if (LocaleHelper.updateLocale(getBaseContext(), this)) {
            this.recreate();
        }

    }


    public void nextButtonClick(android.view.View view) {
        Intent intent = new Intent(this, StudyListActivity.class);
        startActivity(intent);
    }

}
