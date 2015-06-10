package org.rhok.linguist.code;

import java.io.Serializable;

/**
 * Created by lachlan on 7/05/2015.
 */
public class Person implements Serializable {

    public int personid = 0;
    public String name = "";
    public Integer age;
    public String gender = "";
    public String livesin = "";
    public Integer livesinyears;
    public String firstlanguage = "";
    public String secondlanguage = "";
    public String thirdlanguage = "";
    public String otherlanguages = "";
    public String occupation = "";
    public String education = "";
    public Double latitude;
    public Double longitude;

    public PersonWord[] Words;

    public Person() {

    }

    public Person( int _id, String _name, Integer _age, String _gender, String _livesin,
                   Integer _livesinyears, String _firstlanguage, String _secondlanguage,
                   String _thirdlanguage, String _otherlanguages, String _education,
                   Double _latitude, Double _longitude)
    {
        personid = _id;
        name = _name;
        age = _age;
        gender = _gender;
        livesin = _livesin;
        livesinyears = _livesinyears;
        firstlanguage = _firstlanguage;
        secondlanguage = _secondlanguage;
        thirdlanguage = _thirdlanguage;
        otherlanguages = _otherlanguages;
        education = _education;
        latitude = _latitude;
        longitude = _longitude;
    }

    public String getAsJson(PersonWord[] words) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        addField(sb, "name", this.name, true, true);
        addField(sb, "age", this.age, false, true);
        addField(sb, "gender", this.gender, true, true);
        addField(sb, "livesin", this.livesin, true, true);
        addField(sb, "livesinyears", this.livesinyears, false, true);
        addField(sb, "firstlanguage", this.firstlanguage, true, true);
        addField(sb, "secondlanguage", this.secondlanguage, true, true);
        addField(sb, "thirdlanguage", this.thirdlanguage, true, true);
        addField(sb, "otherlanguages", this.otherlanguages, true, true);
        addField(sb, "occupation", this.occupation, true, true);
        addField(sb, "education", this.education, true, true);
        addField(sb, "latitude", this.latitude, false, true);
        addField(sb, "longitude", this.longitude, false, true);
        sb.append("words: [");

        for (int i=0;i<words.length;i++) {
            sb.append("{ itemid: ");
            sb.append(words[i].itemid);
            sb.append(", word: \"");
            if (words[i].word != null) {
                sb.append(words[i].word);
            }
            sb.append("\"");
            sb.append(", audiofilename: \"");
            if (words[i].audiofilename != null) {
                sb.append(words[i].audiofilename);
            }
            sb.append("\"");
            sb.append("}");
            if (i<words.length-1) {
                sb.append(",");
            }
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

    private void addField(StringBuilder sb, String name, Object value, Boolean useQuotes, Boolean addComma) {

        if (value != null) {
            sb.append(name);
            sb.append(": ");
            if (useQuotes) {
                sb.append("\"");
            }
            sb.append(value);
            if (useQuotes) {
                sb.append("\"");
            }
            if (addComma) {
                sb.append(",");
            }
        }
    }

    public String toString()
    {
        return( name );
    }
}
