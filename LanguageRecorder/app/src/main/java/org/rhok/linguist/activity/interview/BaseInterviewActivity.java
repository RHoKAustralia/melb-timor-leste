package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.rhok.linguist.activity.common.AppSettingsActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.activity.common.SplashActivity;
import org.rhok.linguist.activity.old.UploadActivity;
import org.rhok.linguist.code.LocaleHelper;

/**
 * Created by lachlan on 18/06/2015.
 */
public class BaseInterviewActivity extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AppSettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_upload) {
            Intent intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (LocaleHelper.updateLocale(getBaseContext(), this)) {
            this.recreate();
        }

    }

}
