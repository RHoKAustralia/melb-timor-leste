package org.rhok.linguist.api.models;

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
    private String audio_url;
    private String image_url;
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

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
}
