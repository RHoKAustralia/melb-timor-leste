package org.rhok.linguist.network;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.body.StringPart;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.Response;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by marcog on 22/09/2015.
 */
public class MultiPartFormPostService extends IntentService {
    public static final int STATUS_TRANSACTION_COMPLETE = 0;
    public static final int STATUS_TRANSACTION_FAILED = 1;
    public static final int STATUS_TRANSACTION_FAILED_SHOW_MSG = 2;
    public static final long MAX_REQUEST_LENGTH_BYTES = -1;
    public static final String BROADCAST_UPLOAD_RESULT = "org.rhok.linguist.network.BROADCAST_UPLOAD_RESULT";

    public static final String TAG ="MultiPartPost";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    private LocalBroadcastManager mLocalBroadcasterMngr;

    public MultiPartFormPostService() {
        super("org.rhok.linguist.network.MultiPartFormPostService");
        mLocalBroadcasterMngr = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        String request = bundle.getString("request");
        String requestUrl = bundle.getString("url") + request;
        String contentType = bundle.getString("contentType");
        ArrayList<String> filePaths = bundle.getStringArrayList("filePaths");
        Object model = bundle.getSerializable("model");

        ArrayList<Part> fileParts = new ArrayList<Part>();

        if (request.endsWith("addVoice")) {
            //fileParts.add(new StringPart("surveyID", requestModel.getSurveyId().toString()));
            //fileParts.add(new StringPart("MediaType", requestModel.getMediaType().toString()));
            //fileParts.add(new StringPart("Notes", requestModel.getNotes().toString()));
        } else {
            broadcastServiceResult(STATUS_TRANSACTION_FAILED, "Invalid request supplied.");
        }

        if (filePaths == null) {
            filePaths = new ArrayList<String>();
        }


        long totalFileUploadSize = 0;

        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (!file.exists()) {
                broadcastServiceResult(STATUS_TRANSACTION_FAILED, "File(s) provided not found.");
                return;
            }
            totalFileUploadSize += file.length();
            FilePart part = new FilePart(file.getName(), file);
            if (contentType != null)
                part.setContentType(contentType);
            if (MAX_REQUEST_LENGTH_BYTES > -1) {
                if (MAX_REQUEST_LENGTH_BYTES < totalFileUploadSize) {
                    broadcastServiceResult(STATUS_TRANSACTION_FAILED_SHOW_MSG,
                            String.format("The size of the selected upload exceeds the maximum allowed of %d MBs.", MAX_REQUEST_LENGTH_BYTES / (1024L * 1024L)));
                    return;
                }
            }
            fileParts.add(part);
        }

        postFormData(requestUrl, fileParts);
    }

    private void broadcastServiceResult(int status, String message) {
        Intent localIntent = new Intent(BROADCAST_UPLOAD_RESULT);
        localIntent.putExtra("status", status);
        localIntent.putExtra("message", message);
        if (status != STATUS_TRANSACTION_COMPLETE) {
            Log.e(TAG, message);
        } else {
            Log.i(TAG, "Upload Successful");
        }
        mLocalBroadcasterMngr.sendBroadcast(localIntent);
        return;
    }

    private void postFormData(String requestUrl, Iterable<Part> files) {

        // Displays the progress bar for the first time.
        //mNotificationHelper.createNotification();

        Ion.with(getApplicationContext())
                .load(requestUrl)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        long percentage = (uploaded * 100) / total;
                        if (percentage % 5 == 0) {
                          //  mNotificationHelper.progressUpdate((int) percentage);
                        }
                    }
                })
                .setTimeout(60 * 60 * 1000)
                .addMultipartParts(files)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {

                                 @Override
                                 public void onCompleted(Exception e, Response<String> result) {
                               //      mNotificationHelper.completed();
                                     if (e != null) {
                                         broadcastServiceResult(STATUS_TRANSACTION_FAILED, e.getMessage());
                                     } else {
                                         broadcastServiceResult(STATUS_TRANSACTION_COMPLETE, "");
                                     }

                                 }
                             }
                );

    }
}
