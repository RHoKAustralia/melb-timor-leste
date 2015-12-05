package org.rhok.linguist.api;

import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Interviewee;
import org.rhok.linguist.api.models.Interviewer;
import org.rhok.linguist.api.models.Recording;
import org.rhok.linguist.code.entity.Person;

import java.util.List;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class InsertInterviewRequest {
    private Interviewer interviewer;
    private Interviewee interviewee;
    private Interview interview;
    private List<Recording> recordings;

}
