package org.rhok.linguist.api.models;

import org.rhok.linguist.application.LinguistApplication;

import java.io.Serializable;
import java.util.List;

/**
 * A phrase is a question within a Study
 * Created by bramleyt on 5/12/2015.
 */
public class Phrase implements Serializable{

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_TEXT_AUDIO = 3;

    private int id;
    private String english_text;
    /** URL pointing to audio prompt */
    private String audio;
    private String image;
    private int response_type_id;
    private List<String> choices;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish_text() {
        return english_text;
    }

    public void setEnglish_text(String english_text) {
        this.english_text = english_text;
    }

    /** Get URL of audio prompt data */
    public String getAudio() {
        if(audio==null||audio.contains("missing.png"))return null;
        return audio;
    }
    public String formatAudioUrl(){
        String audio = getAudio();
        if(audio!=null) {
            if (audio.toLowerCase().startsWith("http")) return audio;
            if(audio.startsWith("/")) return LinguistApplication.getWebserviceUrl()+audio;
        }
        return audio;
    }

    public boolean hasAudio() {
        return getAudio() != null;
    }

    public boolean hasImage() {
        return getImage() != null;
    }


    /** Set URL of audio prompt data */
    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage() {
        return image;
    }
    public String formatImageUrl(){
        if(image!=null) {
            if (image.toLowerCase().startsWith("http")) return image;
            if(image.startsWith("/")) return LinguistApplication.getWebserviceUrl()+image;
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getResponse_type() {
        return response_type_id;
    }

    public void setResponse_type(int response_type) {
        this.response_type_id = response_type;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public boolean hasChoices() {
        return choices != null && choices.size() > 0;
    }
}
