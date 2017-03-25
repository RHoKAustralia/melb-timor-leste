package org.rhok.linguist.code;

import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.api.models.Study;
import org.rhok.linguist.application.LinguistApplication;
import org.rhok.linguist.util.FileUtils;

import java.io.File;
import java.util.List;

public class StudyDownloader {

    private final String TAG = "StudyDownloader";
    private Context _context;
    private AQuery _aq;

    public StudyDownloader(Context context) {
        _context = context;
        _aq = new AQuery(context);
    }

    public void downloadAll(List<Study> studies) {
        for (Study study : studies) {
            download(study);
        }
    }

    public void download(Study study) {
        Log.d(TAG, "Downloading any missing files for " + study.getName());
        for (Phrase phrase : study.getPhrases()) {
            downloadAudioIfMissing(phrase);
            downloadImageIfMissing(phrase);
        }
        Log.d(TAG, "Downloading complete for " + study.getName());
    }

    private void downloadAudioIfMissing(Phrase phrase) {
        if (phrase.hasAudio()) {
            File audioFile = getPhraseAudioFile(phrase);
            if (!audioFile.exists() || audioFile.length() == 0) {
                downloadUrl(phrase.formatAudioUrl(), audioFile);
            }
        }
    }

    private void downloadImageIfMissing(Phrase phrase) {
        if (phrase.hasImage()) {
            File imageFile = getPhraseImageFile(phrase);
            if (!imageFile.exists() || imageFile.length() == 0) {
                downloadUrl(phrase.formatImageUrl(), imageFile);
            }
        }
    }

    private File getPhraseAudioFile(Phrase phrase) {
        File dir = new File(_context.getFilesDir().getPath(), LinguistApplication.DIR_INTERVIEW_MEDIA);
        return new File(dir, String.format("%d_audio.m4a", phrase.getId()));
    }

    private File getPhraseImageFile(Phrase phrase) {
        if (!phrase.hasImage()) throw new IllegalStateException("phase has no image");
        String ext = FileUtils.getExtension(phrase.getImage());
        File dir = new File(_context.getFilesDir().getPath(), LinguistApplication.DIR_INTERVIEW_MEDIA);
        return new File(dir, phrase.getId() + "_image." + ext);
    }

    private void downloadUrl(String url, File file) {
        Log.d(TAG, "Downloading " + url);
        _aq.download(url, file, new AjaxCallback<File>() {
            @Override
            public void callback(String url, File file, AjaxStatus status) {
                if (file != null && file.exists() && file.length() > 0) {
                    Log.d(TAG, "Download complete: " + url);
                } else {
                    Log.d(TAG, "Download failed: " + url + " message:" + status.getMessage());
                }
            }
        });
    }
}
