package org.rhok.linguist.code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.rhok.linguist.api.models.Interview;
import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.code.entity.PersonWord;
import org.rhok.linguist.network.PCJsonSerializers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lachlan on 4/05/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context _context;

    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "linguist";
    public static final String PERSON_TABLE_NAME = "person";
    public static final String PERSON_COLUMNS =
        "personid, " +
        "name, " +
        "age, " +
        "gender, " +
        "occupation, " +
        "education, " +
        "firstLanguage, " +
        "secondLanguage, " +
        "thirdLanguage, " +
        "fourthLanguage, " +
        "livesInMunicipality, " +
        "livesInDistrict, " +
        "livesInVillage, " +
        "livedWholeLife, " +
        "livedInYears, " +
        "bornMunicipality, " +
        "bornDistrict, " +
        "bornVillage," +
        "uploaded ";

    private static final String PERSONWORD_TABLE_NAME = "personword";

    String createPersonTable = "create table " + PERSON_TABLE_NAME + " " +
            "(personid integer primary key autoincrement," +
            "name varchar, " +
            "age integer, " +
            "gender varchar, "  +
            "occupation varchar, " +
            "education varchar, " +
            "firstlanguage varchar, "  +
            "secondlanguage varchar, "  +
            "thirdlanguage varchar, "  +
            "fourthlanguage varchar, "  +
            "livesinmunicipality varchar, "  +
            "livesindistrict varchar, " +
            "livesinvillage varchar, " +
            "livedwholelife boolean, " +
            "livedinyears integer, "  +
            "bornmunicipality varchar, "  +
            "borndistrict varchar, " +
            "bornvillage varchar," +
            "uploaded boolean); ";

    String createPersonWordTable = "create table " + PERSONWORD_TABLE_NAME + " " +
            "(personwordid integer primary key autoincrement," +
            "personid integer, " +
            "wordid integer, " +
            "wordtranscription varchar, " +
            "audiofilename varchar);";

    private static final String INTERVIEW_TABLE_NAME = "interview";

    String createInterviewTable = "create table " + INTERVIEW_TABLE_NAME + " " +
            "(interviewid integer primary key autoincrement," +
            "completed bit, " +
            "uploaded bit, " +
            "intervieweeid integer, " +
            "interviewerid integer, " +
            "json varchar);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPersonTable);
        db.execSQL(createPersonWordTable);
        db.execSQL(createInterviewTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int fromVersion, int toVersion) {
        if(fromVersion==1){
            db.execSQL("drop table if exists " + INTERVIEW_TABLE_NAME + ";");
            db.execSQL(createInterviewTable);

        }
    }

    private void recreateDB() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("drop table " + PERSONWORD_TABLE_NAME + ";");
        db.execSQL("drop table " + PERSON_TABLE_NAME + ";");
        db.execSQL("drop table " + INTERVIEW_TABLE_NAME + ";");
        db.execSQL(createPersonTable);
        db.execSQL(createPersonWordTable);
        db.execSQL(createInterviewTable);
    }

    public void insertPerson(Person person) {
        //recreateDB();
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getDbValues(person);

        long personId = db.insert(PERSON_TABLE_NAME, null, values);

        person.personid = (int)personId;
        person.uploaded = false;

        db.close();
    }



    private ContentValues getDbValues(Person person) {
        ContentValues values = new ContentValues();
        values.put("name", person.name);
        values.put("age", person.age);
        values.put("gender", person.gender);
        values.put("occupation", person.occupation);
        values.put("education", person.education);
        values.put("firstLanguage", person.firstLanguage);
        values.put("secondLanguage", person.secondLanguage);
        values.put("thirdLanguage", person.thirdLanguage);
        values.put("fourthLanguage", person.fourthLanguage);
        values.put("livesInMunicipality", person.livesInMunicipality);
        values.put("livesInDistrict", person.livesInDistrict);
        values.put("livesInVillage", person.livesInVillage);
        values.put("livedWholeLife", person.livedWholeLife);
        values.put("livedInYears", person.livedInYears);
        values.put("bornMunicipality", person.bornMunicipality);
        values.put("bornDistrict", person.bornDistrict);
        values.put("bornVillage", person.bornVillage);
        values.put("uploaded", person.uploaded);
        return values;
    }

    public void updatePerson(Person person) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getDbValues(person);

        db.update(PERSON_TABLE_NAME, values, "personid = " + String.valueOf(person.personid), null);

        db.close();
    }

    private void updateField(int personId, String fieldName, String strValue, Integer intValue, String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (type.equals("String")) {
            values.put(fieldName, strValue);
        }
        if (type.equals("Integer")) {
            values.put(fieldName, intValue);
        }
        db.update(PERSON_TABLE_NAME, values, "personid = " + String.valueOf(personId), null);

        db.close();
    }

    public void updatePersonAge(int personId, Integer Age) {
        updateField(personId, "age", null, Age, "Integer");
    }

    public void updatePersonGender(int personId, String gender) {
        updateField(personId, "gender", gender, null, "String");
    }

    public void updatePersonOccupation(int personId, String occupation) {
        updateField(personId, "occupation", occupation, null, "String");
    }

    public void updatePersonEducation(int personId, String education) {
        updateField(personId, "education", education, null, "String");
    }

    public void updatePersonLanguage1(int personId, String language) {
        updateField(personId, "firstLanguage", language, null, "String");
    }
    public void updatePersonLanguage2(int personId, String language) {
        updateField(personId, "secondLanguage", language, null, "String");
    }
    public void updatePersonLanguage3(int personId, String language) {
        updateField(personId, "thirdLanguage", language, null, "String");
    }
    public void updatePersonLanguage4(int personId, String language) {
        updateField(personId, "fourthLanguage", language, null, "String");
    }

    public void updatePersonLivesMunicipality(int personId, String location) {
        updateField(personId, "livesInMunicipality", location, null, "String");
    }
    public void updatePersonLivesDistrict(int personId, String location) {
        updateField(personId, "livesInDistrict", location, null, "String");
    }
    public void updatePersonLivesVillage(int personId, String location) {
        updateField(personId, "livesInVillage", location, null, "String");
    }

    public void updatePersonBornMunicipality(int personId, String location) {
        updateField(personId, "bornMunicipality", location, null, "String");
    }
    public void updatePersonBornDistrict(int personId, String location) {
        updateField(personId, "bornDistrict", location, null, "String");
    }
    public void updatePersonBornVillage(int personId, String location) {
        updateField(personId, "bornVillage", location, null, "String");
    }
    public void updatePersonLivedWholeLife(int personId, Boolean livedWholeLife) {
        updateField(personId, "livedWholeLife", null, (livedWholeLife ? 1 : 0), "Integer");
    }
    public void updatePersonLivedYears(int personId, Integer years) {
        updateField(personId, "livedInYears", null, years, "Integer");
    }

    public void insertUpdateInterview(Interview interview){
        ContentValues values = interviewToContentValues(interview);
        SQLiteDatabase db = getWritableDatabase();
        if(interview.get__appid()>0){
            db.update(INTERVIEW_TABLE_NAME, values,  "interviewid = " + String.valueOf(interview.get__appid()), null);
        }
        else{
            long appid = db.insert(INTERVIEW_TABLE_NAME, null, values);
            interview.set__appid((int) appid);
        }
        db.close();
    }
    private ContentValues interviewToContentValues(Interview interview){
        ContentValues v = new ContentValues();
        v.put("completed", interview.is__completed());
        v.put("uploaded", interview.is__uploaded());
        v.put("intervieweeid", interview.get__intervieweeid());
        v.put("interviewerid", interview.get__interviewerid());
        String json = getGson().toJson(interview);
        v.put("json", json);
        return v;
    }
    private Interview getInterviewFromCursor(Cursor c){
        String json = c.getString(c.getColumnIndexOrThrow("json"));
        Interview r = getGson().fromJson(json, Interview.class);
        r.set__appid((int) c.getLong(c.getColumnIndexOrThrow("interviewid")));
        r.set__completed(c.getInt(c.getColumnIndexOrThrow("completed")) > 0);
        r.set__uploaded(c.getInt(c.getColumnIndexOrThrow("uploaded")) > 0);
        r.set__intervieweeid(c.getInt(c.getColumnIndexOrThrow("intervieweeid")));
        r.set__interviewerid(c.getInt(c.getColumnIndexOrThrow("interviewerid")));
        return r;
    }
    public Interview getInterview(int appId){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.rawQuery("select * from " + INTERVIEW_TABLE_NAME + "" +
                    " where interviewid = " + appId, null);
            Interview p = null;
            if (c.moveToNext()) {
                p = getInterviewFromCursor(c);
            }

            db.close();
            return p;

    }
    public List<Interview> getInterviews(boolean readyToUploadOnly) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+INTERVIEW_TABLE_NAME;
        if(readyToUploadOnly) query+=" where completed=1 and uploaded=0";
        Cursor c = db.rawQuery(query, null);

        List<Interview> items = new ArrayList<Interview>();

        while (c.moveToNext()) {
            Interview p = getInterviewFromCursor(c);
            items.add(p);
        }

        db.close();

        return items;
    }
    private Gson getGson(){
        return new GsonBuilder().setExclusionStrategies(PCJsonSerializers.getUnderscoreExclusionStrategy()).create();
    }
    public void insertUpdateRecording(int interviewid, int phraseid, String word, String audiofilename) {
        SQLiteDatabase db = getWritableDatabase();


    }
    public void saveWord(int personid, int wordid, String word, String audiofilename) {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "select personwordid from " + PERSONWORD_TABLE_NAME +
                " where personid = " + String.valueOf(personid) + " and " +
                " wordid = " + String.valueOf(wordid);

        Cursor c = db.rawQuery(sql, null);

        if (c.moveToNext()) {
            int personwordid = c.getInt(0);

            ContentValues values = new ContentValues();
            values.put("wordtranscription", word );
            values.put("audiofilename", audiofilename);

            db.update(PERSONWORD_TABLE_NAME, values,  "personwordid = " + String.valueOf(personwordid), null);
        } else {
            if (word != null || audiofilename != null) {
                ContentValues values = new ContentValues();
                values.put("wordtranscription", word);
                values.put("personid", personid);
                values.put("wordid", wordid);
                values.put("audiofilename", audiofilename);
                db.insert(PERSONWORD_TABLE_NAME, null, values);
            }
        }

        db.close();
    }

    public PersonWord getWord(int personid, int pictureid) {

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select personid, itemid, word, audiofilename from " + PERSONWORD_TABLE_NAME +
                " where personid = " + String.valueOf(personid) + " and " +
                " itemid = " + String.valueOf(pictureid);

        Cursor c = db.rawQuery(sql, null);

        PersonWord word = null;

        if (c.moveToNext()) {
            word = new PersonWord(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3)
            );
        }

        db.close();

        return word;
    }

    public PersonWord[] getAllWords() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select personid, wordid, wordtranscription, audiofilename from " +
                PERSONWORD_TABLE_NAME + " where wordtranscription <> '' or audiofilename <> ''";

        Cursor c = db.rawQuery(sql, null);
        List<PersonWord> items = new ArrayList<PersonWord>();
        while (c.moveToNext()) {
            PersonWord pw = new PersonWord(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3)
            );
            items.add(pw);
        }
        db.close();
        return (PersonWord[]) items.toArray(new PersonWord[items.size()]);
    }

    public String getAllData() {
        Person[] people = getPeople();
        PersonWord[] allWords = getAllWords();

        String installID = Installation.id(_context);
        String json = "{ InstallID: \"" + installID + "\", Data: [";

        for (int i=0;i<people.length;i++) {
            Person p = people[i];
            PersonWord[] words = wordsForPerson(p.personid, allWords);
            json = json.concat(p.getAsJson(words));
            if (i < people.length - 1) {
                json = json.concat(",");
            }
        }


        json = json.concat("]}");
        return json;
    }

    private PersonWord[] wordsForPerson(int personid, PersonWord[] allWords) {
        List<PersonWord> items = new ArrayList<PersonWord>();
        for (int i=0;i<allWords.length;i++) {
            if (allWords[i].personid == personid) {
                items.add(allWords[i]);
            }
        }
        return (PersonWord[]) items.toArray(new PersonWord[items.size()]);
    }


    public PersonWord[] getWordsForPerson(int personId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select personid, itemid, word, audiofilename from " +
                PERSONWORD_TABLE_NAME + " where (word <> '' or audiofilename <> '') " +
                "and personid = " + personId;

        Cursor c = db.rawQuery(sql, null);
        List<PersonWord> items = new ArrayList<PersonWord>();
        while (c.moveToNext()) {
            PersonWord pw = new PersonWord(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3)
            );
            items.add(pw);
        }
        db.close();
        return (PersonWord[]) items.toArray(new PersonWord[items.size()]);
    }

    public void deletePerson(int personId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from personcapture where personid=" + personId + ";");
        db.execSQL("delete from person where personid=" + personId + ";");

        db.close();
    }


    private void resetDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();

       // db.execSQL("drop table person;");
        //db.execSQL("drop table personcapture;");
        db.execSQL(createPersonTable);
        db.execSQL(createPersonWordTable);

        db.close();
    }

    public Person[] getPeople() {
        //resetDatabase();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select " + PERSON_COLUMNS + " from " + PERSON_TABLE_NAME, null);

        List<Person> items = new ArrayList<Person>();

        while (c.moveToNext()) {

            Person p = getPersonFromCursor(c);

            items.add(p);
        }

        db.close();

        return (Person[]) items.toArray(new Person[items.size()]);
    }

    private Person getPersonFromCursor(Cursor c) {

        return new Person(
                c.getInt(0),
                c.getString(1),
                (c.isNull(2) ? null : c.getInt(2)),
                c.getString(3),
                c.getString(4),
                c.getString(5),
                c.getString(6),
                c.getString(7),
                c.getString(8),
                c.getString(9),
                c.getString(10),
                c.getString(11),
                c.getString(12),
                (c.isNull(13) ? null : (c.getInt(13) == 1)),
                (c.isNull(14) ? null : c.getInt(14)),
                c.getString(15),
                c.getString(16),
                c.getString(17),
                (c.isNull(18) ? null : (c.getInt(18) == 1))
        );
    }

    public Person getPerson(int personId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select " + PERSON_COLUMNS + " from " + PERSON_TABLE_NAME + "" +
                " where personid = " + personId, null);

        Person p = null;

        if (c.moveToNext()) {
            p = getPersonFromCursor(c);
        }

        db.close();

        return p;
    }

}