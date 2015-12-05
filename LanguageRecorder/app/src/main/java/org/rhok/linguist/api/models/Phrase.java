package org.rhok.linguist.api.models;

import java.util.List;

/**
 * A phrase is a question within a Study
 * Created by bramleyt on 5/12/2015.
 */
public class Phrase {

    private int id;
    private String english_text;
    private String audio_url;
    private String image_url;
    private List<String> choices;

}
