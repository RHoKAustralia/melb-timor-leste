package org.rhok.linguist.code;

import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.api.models.Study;

import java.io.File;
import java.util.HashSet;
import java.util.List;

public class StudyDownloader {

    private final String TAG = "StudyDownloader";
    private AQuery _aq;
    private HashSet<File> _downloading;
    private Runnable _onDownloadComplete;

    public StudyDownloader(Context context) {
        _aq = new AQuery(context);
        _downloading = new HashSet<>();
    }

    public void downloadAll(List<Study> studies, Runnable onComplete) {
        _onDownloadComplete = onComplete;
        for (Study study : studies) {
            download(study);
        }
    }

    private void download(Study study) {
        Log.d(TAG, "Downloading any missing files for " + study.getName());
        for (Phrase phrase : study.getPhrases()) {
            downloadAudioIfMissing(phrase);
            downloadImageIfMissing(phrase);
        }
        if (_downloading.isEmpty()) {
            onAllDownloadsComplete();
        }
    }

    private void downloadAudioIfMissing(Phrase phrase) {
        if (phrase.hasAudio()) {
            File audioFile = DiskSpace.getPhraseAudio(phrase);
            if (!audioFile.exists() || audioFile.length() == 0) {
                downloadUrl(phrase.formatAudioUrl(), audioFile);
            }
        }
    }

    private void downloadImageIfMissing(Phrase phrase) {
        if (phrase.hasImage()) {
            File imageFile = DiskSpace.getPhraseImage(phrase);
            if (!imageFile.exists() || imageFile.length() == 0) {
                downloadUrl(phrase.formatImageUrl(), imageFile);
            }
        }
    }

    private void downloadUrl(String url, File file) {
        Log.d(TAG, "Downloading " + url);
        _downloading.add(file);
        _aq.download(url, file, new AjaxCallback<File>() {
            @Override
            public void callback(String url, File file, AjaxStatus status) {
                if (file == null || !file.exists() || file.length() == 0) {
                    Log.d(TAG, "Download failed: " + url + " message:" + status.getMessage());
                }
                onFileDownloadComplete(file);
            }
        });
    }

    private void onFileDownloadComplete(File file) {
        if (_downloading.contains(file)) {
            _downloading.remove(file);
        }
        if (_downloading.isEmpty()) {
            onAllDownloadsComplete();
        }
    }

    private void onAllDownloadsComplete() {
        if (_onDownloadComplete != null) _onDownloadComplete.run();
    }
}
