package org.rhok.linguist.code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.rhok.linguist.code.entity.Person;
import org.rhok.linguist.code.entity.PersonWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lachlan on 4/05/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context _context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "linguist";
    private static final String PERSON_TABLE_NAME = "person";
    private static final String PERSON_COLUMNS = 
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
        "livesInYears, " +
        "bornMunicipality, " +
        "bornDistrict, " +
        "bornVillage ";

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
            "bornvillage varchar); ";

    String createPersonWordTable = "create table " + PERSONWORD_TABLE_NAME + " " +
            "(personwordid integer primary key autoincrement," +
            "personid integer, " +
            "wordid integer, " +
            "wordtranscription varchar, " +
            "audiofilename varchar);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPersonTable);
        db.execSQL(createPersonWordTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int fromVersion, int toVersion) {

    }

    public void insertPerson(Person person) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getDbValues(person);

        long personId = db.insert(PERSON_TABLE_NAME, null, values);

        person.personid = (int)personId;

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
        values.put("livesInYears", person.livesInYears);
        values.put("bornMunicipality", person.bornMunicipality);
        values.put("bornDistrict", person.bornDistrict);
        values.put("bornVillage", person.bornVillage);
        return values;
    }

    public void updatePerson(Person person) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getDbValues(person);

        db.update(PERSON_TABLE_NAME, values, "personid = " + String.valueOf(person.personid), null);

        db.close();
    }

    public void saveWord(int personid, int pictureid, String word, String audiofilename) {

        SQLiteDatabase db = this.getWritableDatabase();

/*        db.execSQL("drop table " + PERSONCAPTURE_TABLE_NAME + ";");
        db.execSQL("drop table " + PERSON_TABLE_NAME + ";");
        db.execSQL(createPersonTable);
        db.execSQL(createPersonCaptureTable);*/

        String sql = "select personcaptureid from " + PERSONWORD_TABLE_NAME +
                " where personid = " + String.valueOf(personid) + " and " +
                " itemid = " + String.valueOf(pictureid);

        Cursor c = db.rawQuery(sql, null);

        if (c.moveToNext()) {
            int personcaptureid = c.getInt(0);

            ContentValues values = new ContentValues();
            values.put("word", word );
            values.put("audiofilename", audiofilename);

            db.update(PERSONWORD_TABLE_NAME, values,  "personcaptureid = " + String.valueOf(personcaptureid), null);
        } else {
            if (word != null || audiofilename != null) {
                ContentValues values = new ContentValues();
                values.put("word", word);
                values.put("personid", personid);
                values.put("itemid", pictureid);
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
        String sql = "select personid, itemid, word, audiofilename from " +
                PERSONWORD_TABLE_NAME + " where word <> '' or audiofilename <> ''";

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

        db.execSQL("drop table person;");
        db.execSQL("drop table personcapture;");
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
                c.getString(17)
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