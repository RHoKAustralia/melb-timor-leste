package org.rhok.linguist.activity.old;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.rhok.linguist.R;
import org.rhok.linguist.code.DatabaseHelper;
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.code.entity.PersonWord;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;


public class UploadActivity extends ActionBarActivity {

    private String baseUrl = "http://bluetac";
    private TextView uploadProgressTextView;
    private Button uploadFileButton;
    private String progressText;
    private TextView dataInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadFileButton = (Button)findViewById(R.id.upload_file_button);
        uploadProgressTextView = (TextView)findViewById(R.id.upload_progress);
        dataInfo = (TextView)findViewById(R.id.data_info);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Person[] people = db.getPeople();

        String numberOfPeopleText = getResources().getString(R.string.upload_number_of_people_label);
        dataInfo.setText(numberOfPeopleText + ": " + people.length);

        progressText = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload, menu);
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





    public void uploadToServer(android.view.View view) {

        new Thread(new Runnable() {
            public void run() {

                addMessage( getResources().getString(R.string.upload_starting_upload) + "...");
                addMessage( getResources().getString(R.string.upload_uploading_data) + "...");

                uploadData();

                uploadAudioData();

                addMessage( getResources().getString(R.string.upload_upload_complete));

            }
        }).start();
    }

    private void sendNotification(String message) {

        Intent resultIntent = new Intent(this, UploadActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.livru_timor_logo)
                        .setContentTitle("Local Linguist")
                        .setContentText(message);

        // notification click behaviour
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void addMessage(String message) {
        Date now = new Date();
        String thetime = DateFormat.getTimeInstance().format(now);
        progressText += thetime + " " + message + "\n";
        uploadProgressTextView.post(new Runnable() {
            public void run() {
                uploadProgressTextView.setText(progressText);
            }
        });

    }

    private void uploadData() {
        HttpParams httpParameters = new BasicHttpParams();
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(baseUrl + "/api/languagedata");

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            String json = null;//dbHelper.getAllData();

            Log.i("LanguageApp", json);

            StringEntity se = new StringEntity(json, HTTP.UTF_8);

            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            Log.i("languageapp", e.toString());
            sendNotification("Data upload failed");
            addMessage(e.toString());

            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i("languageapp", e.toString());
            sendNotification("Data upload failed");
            addMessage(e.toString());
        }

    }

    private void uploadAudioData() {

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        PersonWord[] words = null;//dbHelper.getAllWords();

        if (words == null) {
            return;
        }

        for (int i=0;i<words.length;i++) {
            String audiofilename = words[i].audiofilename;

            if (audiofilename != null)
            {
                if (audiofilename.length() > 0)
                {
                    File f = DiskSpace.getInterviewRecording(audiofilename);

                    if (f.exists()) {

                        String msg = getResources().getString(R.string.upload_uploading_audio);
                        if (words[i].word != null && words.length > 0) {
                            addMessage(msg + ": " + audiofilename);
                        }
                        doFileUpload(f.getAbsolutePath(), audiofilename);
                    }
                }
            }
        }
    }


    private void doFileUpload(String audioFilename, String shortName){
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;
        String responseFromServer = "";
        String urlString = baseUrl + "/api/audiodata";

        try
        {

            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(new File(audioFilename) );
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            conn.setRequestProperty("uploaded_file", shortName);
            dos = new DataOutputStream( conn.getOutputStream() );
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + shortName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.i("LanguageApp", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        }
        catch (MalformedURLException ex)
        {
            Log.e("LanguageApp", "error: " + ex.getMessage(), ex);
        }
        catch (IOException ioe)
        {
            Log.e("LanguageApp", "error: " + ioe.getMessage(), ioe);
        }


        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream ( conn.getInputStream() );
            String str;

            while (( str = inStream.readLine()) != null)
            {
                Log.i("LanguageApp","Server Response: "+str);
            }
            inStream.close();

        }
        catch (IOException ioex){
            Log.e("LanguageApp", "error: " + ioex.getMessage(), ioex);
        }
    }


}
