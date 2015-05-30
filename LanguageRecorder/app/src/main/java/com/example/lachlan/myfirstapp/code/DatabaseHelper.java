package com.example.lachlan.myfirstapp.code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by lachlan on 4/05/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "lachtest";
    private static final String PERSON_TABLE_NAME = "person";
    private static final String PERSON_COLUMNS = "personid, name, age, gender, livesin, livesinyears, " +
            "firstlanguage, secondlanguage, thirdlanguage, otherlanguages, education, " +
            "latitude, longitude";

    private static final String PERSONCAPTURE_TABLE_NAME = "personcapture";

    private Context _context;

    String createPersonTable = "create table " + PERSON_TABLE_NAME + " " +
            "(personid integer primary key autoincrement," +
            "name varchar, " +
            "age integer, " +
            "gender varchar, "  +
            "livesin varchar, "  +
            "livesinyears integer, "  +
            "firstlanguage varchar, "  +
            "secondlanguage varchar, "  +
            "thirdlanguage varchar, "  +
            "otherlanguages varchar, "  +
            "education varchar, " +
            "latitude real," +
            "longitude real);";

    String createPersonCaptureTable = "create table " + PERSONCAPTURE_TABLE_NAME + " " +
            "(personcaptureid integer primary key autoincrement," +
            "personid integer, " +
            "itemid integer, " +
            "word varchar, " +
            "audiofilename varchar);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createPersonTable);
        db.execSQL(createPersonCaptureTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int fromVersion, int toVersion) {

    }

    private ContentValues getDbValues(Person person) {
        ContentValues values = new ContentValues();
        values.put("name", person.name  );
        values.put("age", person.age);
        values.put("gender", person.gender);
        values.put("livesin", person.livesin);
        values.put("livesinyears", person.livesinyears);
        values.put("firstlanguage", person.firstlanguage);
        values.put("secondlanguage", person.secondlanguage);
        values.put("thirdlanguage", person.thirdlanguage);
        values.put("otherlanguages", person.otherlanguages);
        values.put("education", person.education);
        values.put("latitude", person.latitude);
        values.put("longitude", person.longitude);
        return values;
    }

    public void insertPerson(Person person) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getDbValues(person);

        long personid = db.insert(PERSON_TABLE_NAME, null, values);

        person.personid = (int)personid;

        db.close();
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

        String sql = "select personcaptureid from " + PERSONCAPTURE_TABLE_NAME +
                " where personid = " + String.valueOf(personid) + " and " +
                " itemid = " + String.valueOf(pictureid);

        Cursor c = db.rawQuery(sql, null);

        if (c.moveToNext()) {
            int personcaptureid = c.getInt(0);

            ContentValues values = new ContentValues();
            values.put("word", word );
            values.put("audiofilename", audiofilename);

            db.update(PERSONCAPTURE_TABLE_NAME, values,  "personcaptureid = " + String.valueOf(personcaptureid), null);
        } else {
            if (word != null || audiofilename != null) {
                ContentValues values = new ContentValues();
                values.put("word", word);
                values.put("personid", personid);
                values.put("itemid", pictureid);
                values.put("audiofilename", audiofilename);
                db.insert(PERSONCAPTURE_TABLE_NAME, null, values);
            }
        }

        db.close();
    }

    public PersonWord getWord(int personid, int pictureid) {

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "select personid, itemid, word, audiofilename from " + PERSONCAPTURE_TABLE_NAME +
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
                PERSONCAPTURE_TABLE_NAME + " where word <> '' or audiofilename <> ''";

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
                PERSONCAPTURE_TABLE_NAME + " where (word <> '' or audiofilename <> '') " +
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
        db.execSQL(createPersonCaptureTable);

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
        Integer age = null;
        Integer livesInYears = null;

        if (!c.isNull(2))
        {
            age = c.getInt(2);
        }
        if (!c.isNull(5)) {
            livesInYears = c.getInt(5);
        }

        return new Person(
                c.getInt(0),
                c.getString(1),
                age,
                c.getString(3),
                c.getString(4),
                livesInYears,
                c.getString(6),
                c.getString(7),
                c.getString(8),
                c.getString(9),
                c.getString(10),
                c.getDouble(11),
                c.getDouble(12)
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