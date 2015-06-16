package org.rhok.linguist.recording;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.rhok.linguist.R;
import org.rhok.linguist.interview.InterviewLivedLengthActivity;
import org.rhok.linguist.location.MunicipalityActivity;

public class RecordingListenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_listen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recording_listen, menu);
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

    public void noButtonClick(View view) {

        Intent intent = new Intent(this, RecordingAudioActivity.class);
        startActivity(intent);
    }

    public void yesButtonClick(View view) {

        Intent intent = new Intent(this, RecordingTranscribeActivity.class);
        startActivity(intent);
    }

}
