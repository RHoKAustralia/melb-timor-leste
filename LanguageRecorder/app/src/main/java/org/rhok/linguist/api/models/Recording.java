package org.rhok.linguist.api.models;

import java.util.Date;

/**
 * A recording is an answer to a phrase question
 * Created by bramleyt on 5/12/2015.
 */
public class Recording {

    private int id;
    private Date recorded;
    private int interview_id;
    private int language_id;
    private int phrase_id;

    private String audio_url;
    private String text_response;

}
