package org.rhok.linguist;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.code.DropBoxSettings;
import org.rhok.linguist.code.Person;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SettingsActivity extends ActionBarActivity {

    final static public String PREFS_NAME = "MyPrefsFile";


    // In the class declaration section:
    private DropboxAPI<AndroidAuthSession> mDBApi;
    private boolean isLoggedIn;

    private TextView editTextTotalDiskSpace;
    private TextView editTextTotalDiskSpaceFree;
    private TextView dropboxAccessToken;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextTotalDiskSpace = (TextView)findViewById(R.id.total_disk_space_label);
        editTextTotalDiskSpaceFree = (TextView)findViewById(R.id.total_disk_space_free_label);
        dropboxAccessToken = (TextView)findViewById(R.id.dropbox_access_token);
        loginButton = (Button)findViewById(R.id.connect_dropbox_button);

        String diskSpace = DiskSpace.totalDiskSpace();
        String freeSpace = DiskSpace.totalAvailableDiskSpace();

        String diskSpaceLabel = getResources().getString(R.string.settings_disk_size_label);
        String diskFreeLabel = getResources().getString(R.string.settings_disk_free_label);

        editTextTotalDiskSpace.setText(diskSpaceLabel + ": " + diskSpace);
        editTextTotalDiskSpaceFree.setText(diskFreeLabel + ": " + freeSpace);

        /*loggedIn(false);


        AppKeyPair pair = new AppKeyPair(DropBoxSettings.APP_KEY, DropBoxSettings.APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(pair);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        */
    }

    public void loggedIn(boolean isLogged) {
        //isLoggedIn = isLogged;
        //loginButton.setText(isLogged ? "Log out of dropbox" : "Log in to dropbox");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_disk_space, menu);
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

    public void connectToDropBox(android.view.View view) {
        /*if (isLoggedIn) {
            mDBApi.getSession().unlink();
            loggedIn(false);
        } else {

            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String accessToken = settings.getString("dropboxAccessToken", null);

            if (accessToken == null) {
                mDBApi.getSession().startOAuth2Authentication(SettingsActivity.this);
            } else {
                mDBApi.getSession().setOAuth2AccessToken(accessToken);
                loggedIn(true);
            }

        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*if (mDBApi == null) {
            Log.e("myfirstapp", "mDBApi is null in resume, what do I do now??");
        } else {
            AndroidAuthSession session = mDBApi.getSession();
            if (session.authenticationSuccessful()) {
                try {
                    session.finishAuthentication();
                    String accessToken = mDBApi.getSession().getOAuth2AccessToken();

                    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("dropboxAccessToken", accessToken);
                    editor.commit();

                    loggedIn(true);
                } catch (IllegalStateException e) {
                    Log.e("myfirstapp", e.toString());
                }
            }
        }*/
    }



    public void uploadFile(android.view.View view)
    {
        /*dropboxAccessToken.setText("Uploading...");

        //uploadFileButton.setEnabled(false);

        new Thread(new Runnable() {
            public void run() {

                try {

                    for (int i=1;i<=5;i++) {

                        String filename = DiskSpace.getFilename(i);
                        File file = new File(filename);

                        if (file.exists()) {
                            FileInputStream inputStream = new FileInputStream(file);

                            DropboxAPI.Entry response = mDBApi.putFileOverwrite(
                                    "/audio-" + i + ".3gp", inputStream, file.length(), null);

                            //Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
                        }
                    }

                    dropboxAccessToken.post(new Runnable() {
                        public void run() {
                            dropboxAccessToken.setText("File uploaded!");
                            //uploadFileButton.setEnabled(true);
                        }
                    });
                }
                catch (Exception e2) {
                    Log.e("DbExampleLog", e2.toString());

                    dropboxAccessToken.post(new Runnable() {
                        public void run() {
                            dropboxAccessToken.setText("An error occured...");
                            //uploadFileButton.setEnabled(true);
                        }
                    });
                }
            }
        }).start();*/
    }
}

