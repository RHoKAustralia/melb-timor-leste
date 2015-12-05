package org.rhok.linguist.api.models;

import java.util.Date;
import java.util.List;

/**
 * An interview is a collection of Recording answers to a study
 * Created by bramleyt on 5/12/2015.
 */
public class Interview {

    private int id;
    private Date interview_time;
    private int study_id;
    private int interviewer_id;
    private int interviewee_id;
    private int locale_id;

    private List<Recording> recordings;
}
