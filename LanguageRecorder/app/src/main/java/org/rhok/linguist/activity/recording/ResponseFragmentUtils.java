package org.rhok.linguist.activity.recording;

import android.util.Log;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.util.Reflect;

import java.io.File;

class ResponseFragmentUtils {

    private static final String TAG = "ResponseFragmentUtils";

    static void showImagePrompt(final ImageView imageView, final Phrase phrase) {
        final AQuery aq = new AQuery(imageView);
        if (!phrase.hasImage()) {
            aq.id(imageView).gone();
        }
        else if (phrase.formatImageUrl().startsWith("http")) {
            loadPhraseImage(aq, phrase, new Runnable() {
                @Override
                public void run() {
                    aq.id(imageView).image(DiskSpace.getPhraseImage(phrase), imageView.getWidth());
                }
            });
        }
        else {
            // in case it refers to a built-in image, eg "word4"
            // todo: is this still needed now that images are loaded from website?
            int resId = Reflect.getImageResId(phrase.getImage());
            aq.id(imageView).image(resId);
        }
    }

    private static void loadPhraseImage(AQuery aq, Phrase phrase, final Runnable onLoadComplete) {
        final File imageFile = DiskSpace.getPhraseImage(phrase);
        if (imageFile.exists() && imageFile.length() > 0) {
            onLoadComplete.run();
        } else {
            aq.download(phrase.formatAudioUrl(), imageFile, new AjaxCallback<File>() {
                @Override
                public void callback(String url, File file, AjaxStatus status) {
                    if (file != null && file.exists() && file.length() > 0) {
                        onLoadComplete.run();
                    }
                    else {
                        Log.e(TAG, "failed to load image :" + status.getError());
                    }
                }
            });
        }
    }
}
