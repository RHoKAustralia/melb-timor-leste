package org.rhok.linguist.api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An interview is a collection of Recording answers to a study
 * Created by bramleyt on 5/12/2015.
 */
public class Interview implements Serializable{

    /**
     * id of interview in local db
     */
    private int __appid;
    /**
     * if this interview is completed
     */
    private boolean __completed;
    /**
     * if this interview has been uploaded
     */
    private boolean __uploaded;
    /**
     * id of interviewee (person) in local db
     */
    private int __intervieweeid;
    /**
     * id of interviewer (user) in local db. NYI.
     */
    private int __interviewerid;
    private int id;
    private Date interview_time;
    private int study_id;
    private int interviewer_id;
    private int interviewee_id;
    private int locale_id;

    private List<Recording> recordings;

    public Interview(){

    }
    public Interview(Study study){
        interview_time= new Date();
        study_id=study.getId();
        recordings=new ArrayList<Recording>(study.getPhrases().size());
        for(Phrase phrase : study.getPhrases()){
            Recording recording = new Recording();
            recording.setPhrase_id(phrase.getId());
            recordings.add(recording);
        }
    }

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

    public Date getInterview_time() {
        return interview_time;
    }

    public void setInterview_time(Date interview_time) {
        this.interview_time = interview_time;
    }

    public int getStudy_id() {
        return study_id;
    }

    public void setStudy_id(int study_id) {
        this.study_id = study_id;
    }

    public int getInterviewer_id() {
        return interviewer_id;
    }

    public void setInterviewer_id(int interviewer_id) {
        this.interviewer_id = interviewer_id;
    }

    public int getInterviewee_id() {
        return interviewee_id;
    }

    public void setInterviewee_id(int interviewee_id) {
        this.interviewee_id = interviewee_id;
    }

    public int getLocale_id() {
        return locale_id;
    }

    public void setLocale_id(int locale_id) {
        this.locale_id = locale_id;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }

    public boolean is__completed() {
        return __completed;
    }

    public void set__completed(boolean __completed) {
        this.__completed = __completed;
    }

    public boolean is__uploaded() {
        return __uploaded;
    }

    public void set__uploaded(boolean __uploaded) {
        this.__uploaded = __uploaded;
    }

    public int get__intervieweeid() {
        return __intervieweeid;
    }

    public void set__intervieweeid(int __intervieweeid) {
        this.__intervieweeid = __intervieweeid;
    }

    public int get__interviewerid() {
        return __interviewerid;
    }

    public void set__interviewerid(int __interviewerid) {
        this.__interviewerid = __interviewerid;
    }
}
