package org.rhok.linguist.api;

import net.servicestack.client.IReturn;

import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.api.models.Interviewee;
import org.rhok.linguist.api.models.Interviewer;
import org.rhok.linguist.api.models.Recording;
import org.rhok.linguist.code.entity.Person;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class InsertInterviewRequest implements Serializable, IReturn<Interview>{
    public Interviewer interviewer;
    public Interviewee interviewee;
    public Interview interview;

    @Override
    public Object getResponseType() {
        return Interview.class;
    }
}
