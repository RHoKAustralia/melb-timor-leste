package org.rhok.linguist.api.models;

import java.io.Serializable;
import java.util.Date;

/**
 * A recording is an answer to a phrase question
 * Created by bramleyt on 5/12/2015.
 */
public class Recording implements Serializable{

    private int __appid;
    private int id;
    private Date recorded;
    private int interview_id;
    private int language_id;
    private int phrase_id;

    private String audio_url;
    private String text_response;

    /**
     * id as stored in API
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * ID as stored in app database. Not serialized to json
     * @return
     */
    public int get__appid() {
        return __appid;
    }

    /**
     * ID as stored in app database. Not serialized to json
     * @param __appid
     */
    public void set__appid(int __appid) {
        this.__appid = __appid;
    }

    public Date getRecorded() {
        return recorded;
    }

    public void setRecorded(Date recorded) {
        this.recorded = recorded;
    }

    public int getInterview_id() {
        return interview_id;
    }

    public void setInterview_id(int interview_id) {
        this.interview_id = interview_id;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public int getPhrase_id() {
        return phrase_id;
    }

    public void setPhrase_id(int phrase_id) {
        this.phrase_id = phrase_id;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getText_response() {
        return text_response;
    }

    public void setText_response(String text_response) {
        this.text_response = text_response;
    }
}
