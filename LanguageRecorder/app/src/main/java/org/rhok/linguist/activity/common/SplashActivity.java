package org.rhok.linguist.activity.common;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.rhok.linguist.R;
import org.rhok.linguist.activity.interview.InterviewNameActivity;
import org.rhok.linguist.code.LocaleHelper;


public class SplashActivity extends ActionBarActivity {

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
            Intent intent = new Intent(this, AppSettingsActivity.class);
            startActivity(intent);
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
        Intent intent = new Intent(this, InterviewNameActivity.class);
        startActivity(intent);
    }

}
