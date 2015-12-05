package org.rhok.linguist.api.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A study contains a list of Phrases (interview questions)
 * Created by bramleyt on 5/12/2015.
 */
public class Study implements Serializable{

    private int id;
    private String name;
    private String instructions;
    private String language_id;
    private Date start_date;
    private List<Phrase> phrases;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public List<Phrase> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
