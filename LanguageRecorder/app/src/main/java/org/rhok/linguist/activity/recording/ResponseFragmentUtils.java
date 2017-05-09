package org.rhok.linguist.activity.recording;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.rhok.linguist.R;
import org.rhok.linguist.api.models.Phrase;
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.util.Reflect;

import java.io.File;

class ResponseFragmentUtils {

    private static final String TAG = "ResponseFragmentUtils";

    static String getPromptText(Context context, Phrase phrase) {
        String phrasePromptText = phrase.getEnglish_text();
        if (phrasePromptText != null && phrasePromptText.length() > 0)
            return phrasePromptText;
        String inputInstruction = getInputInstruction(context, phrase);
        String outputInstruction = getOutputInstruction(context, phrase);
        return context.getString(R.string.phrase_prompt_text)
                .replace("##input_instruction##", inputInstruction)
                .replace("##output_instruction##", outputInstruction);
    }

    private static String getInputInstruction(Context context, Phrase phrase) {
        if (phrase.hasAudio())
            return context.getString(R.string.phrase_prompt_text_input_audio);
        if (phrase.hasImage())
            return context.getString(R.string.phrase_prompt_text_input_picture);
        // probably not right if we get here...
        return context.getString(R.string.phrase_prompt_text_input_picture);
    }

    private static String getOutputInstruction(Context context, Phrase phrase) {
        if (phrase.getResponse_type() == Phrase.TYPE_AUDIO)
            return context.getString(R.string.phrase_prompt_text_output_audio);
        if (phrase.getResponse_type() == Phrase.TYPE_TEXT) {
            if (phrase.hasChoices())
                return context.getString(R.string.phrase_prompt_text_output_choices);
            else
                return context.getString(R.string.phrase_prompt_text_output_text);
        }
        if (phrase.getResponse_type() == Phrase.TYPE_TEXT_AUDIO) {
            if (phrase.hasChoices())
                return context.getString(R.string.phrase_prompt_text_output_audio_choices);
            else
                return context.getString(R.string.phrase_prompt_text_output_audio_text);
        }
        // probably not right if we get here...
        return context.getString(R.string.phrase_prompt_text_output_text);
    }

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
