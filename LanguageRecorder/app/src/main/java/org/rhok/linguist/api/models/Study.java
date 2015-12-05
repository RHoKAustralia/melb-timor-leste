package org.rhok.linguist.api.models;

import java.util.Date;
import java.util.List;

/**
 * A study contains a list of Phrases (interview questions)
 * Created by bramleyt on 5/12/2015.
 */
public class Study {

    private int id;
    private String name;
    private String language_id;
    private Date start_date;
    private List<Phrase> phrases;
}
