package org.rhok.linguist.code.entity;

import org.rhok.linguist.api.models.Recording;

import java.io.Serializable;
import java.util.Date;

/**
 * A recording is an answer to a phrase question
 * Created by bramleyt on 5/12/2015.
 */
public class RecordingEntity implements Serializable{

    public int recordingid;
    public Date recorded;
    public int interviewid;
    public int languageid;
    public int phraseid;

    public String audiofilename;
    public String word;

    public Recording toApiModel(){
        Recording r = new Recording();
        r.setRecorded(recorded);
        r.setInterview_id(interviewid);
        r.setLanguage_id(languageid);
        r.setPhrase_id(phraseid);
        r.set__audio_filename(audiofilename);
        r.setText_response(word);
        return r;
    }
   
}
