package org.rhok.linguist.activity.interview;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import org.rhok.linguist.activity.common.AppSettingsActivity;
import org.rhok.linguist.R;
import org.rhok.linguist.activity.common.BaseActivity;
import org.rhok.linguist.activity.common.SplashActivity;

/**
 * Created by lachlan on 18/06/2015.
 */
public abstract class BaseInterviewActivity extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interview_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.action_cancel: {
                Intent intent = new Intent(this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
            case R.id.action_settings: {
                Intent intent = new Intent(this, AppSettingsActivity.class);
                startActivity(intent);
                return true;

            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
