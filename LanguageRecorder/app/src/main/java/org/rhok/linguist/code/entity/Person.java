package org.rhok.linguist.code.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by lachlan on 7/05/2015.
 */
public class Person implements Serializable {

    public int personid = 0;

    public String name;
    public Integer age;
    public String gender;
    public String occupation;
    public String education;
    public String firstLanguage;
    public String secondLanguage;
    public String thirdLanguage;
    public String fourthLanguage;
    public String livesInMunicipality;
    public String livesInDistrict;
    public String livesInVillage;
    public Boolean livedWholeLife;
    public Integer livedInYears;
    public String bornMunicipality;
    public String bornDistrict;
    public String bornVillage;
    public Boolean uploaded;

    public PersonWord[] Words;

    public Person() {

    }

    public Person (String _name) {
        name = _name;
    }

    public Person( int _id,
                   String _name,
                   Integer _age,
                   String _gender,
                   String _occupation,
                   String _education,
                   String _firstLanguage,
                   String _secondLanguage,
                   String _thirdLanguage,
                   String _fourthLanguage,
                   String _livesInMunicipality,
                   String _livesInDistrict,
                   String _livesInVillage,
                   Boolean _livedWholeLife,
                   Integer _livesInYears,
                   String _bornMunicipality,
                   String _bornDistrict,
                   String _bornVillage,
                   Boolean _uploaded
                 )
    {
        personid = _id;
        name = _name;
        age = _age;
        gender = _gender;
        occupation = _occupation;
        education = _education;
        firstLanguage = _firstLanguage;
        secondLanguage = _secondLanguage;
        thirdLanguage = _thirdLanguage;
        fourthLanguage = _fourthLanguage;
        livesInMunicipality = _livesInMunicipality;
        livesInDistrict = _livesInDistrict;
        livesInVillage = _livesInVillage;
        livedWholeLife = _livedWholeLife;
        livedInYears = _livesInYears;
        bornMunicipality = _bornMunicipality;
        bornDistrict = _bornDistrict;
        bornVillage = _bornVillage;
        uploaded = _uploaded;
    }

    public String getAsJson(PersonWord[] words) {

        org.json.JSONObject json = new JSONObject();

        try {
            json.put("personid", this.personid);
            json.put("name", this.name);
            json.put("age", this.age);
            json.put("gender", this.gender);
            json.put("occupation", this.occupation);
            json.put("education", this.education);

            json.put("firstLanguage", this.firstLanguage);
            json.put("secondLanguage", this.secondLanguage);
            json.put("thirdLanguage", this.thirdLanguage);
            json.put("fourthLanguage", this.fourthLanguage);

            json.put("livesInMunicipality", this.livesInMunicipality);
            json.put("livesInDistrict", this.livesInDistrict);
            json.put("livesInVillage", this.livesInVillage);

            json.put("livedWholeLife", this.livedWholeLife);
            json.put("livedInYears", this.livedInYears);

            json.put("bornMunicipality", this.bornMunicipality);
            json.put("bornDistrict", this.bornDistrict);
            json.put("bornVillage", this.bornVillage);

            JSONArray jsonWords = new JSONArray();

            for (int i=0;i<words.length;i++) {

                if (words[i].word != null || words[i].audiofilename != null) {
                    JSONObject jsonWord = new JSONObject();

                    jsonWord.put("wordid", words[i].itemid);

                    if (words[i].word != null) {
                        jsonWord.put("word", words[i].word);
                    }
                    if (words[i].audiofilename != null) {
                        jsonWord.put("filename", words[i].audiofilename);
                    }
                    jsonWords.put(jsonWord);
                }
            }

            json.put("words", jsonWords);

            return json.toString();
        }
        catch (JSONException jsonE) {
            return jsonE.toString();
        }
    }

    public String toString()
    {
        return( name );
    }
}
