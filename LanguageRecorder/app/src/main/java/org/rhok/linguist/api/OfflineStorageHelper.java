package org.rhok.linguist.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.rhok.linguist.R;
import org.rhok.linguist.api.models.Study;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class OfflineStorageHelper {
    private Context context;
    public OfflineStorageHelper(Context c){
        this.context=c;
    }
    private static final String FILE_STUDY_LIST = "studies.json";
    private static final String BASE_DIRECTORY = "offline";
    public List<Study> getStudyListFromAssets(int rawResId){
        Type type = new TypeToken<List<Study>>(){}.getType();
        try {
            InputStream inputStream = context.getResources().openRawResource(rawResId);
            InputStreamReader reader = new InputStreamReader(inputStream);
            return getGson().fromJson(reader, type);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

        }
        return null;
    }
    public List<Study> getSavedStudyList(){
        Type type = new TypeToken<List<Study>>(){}.getType();
        File file = new File(context.getDir(BASE_DIRECTORY, 0), FILE_STUDY_LIST);
        if(!file.exists()) return null;
        Gson gson = getGson();
        FileReader reader=null;
        try{
            reader = new FileReader(file);
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            try{reader.close();}catch(Exception e){}
        }
        return null;
    }
    public boolean writeStudyList( List<Study> studies){
        Gson gson = new Gson();
        File file = new File(context.getDir(BASE_DIRECTORY, 0), FILE_STUDY_LIST);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            gson.toJson(studies, writer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{writer.close();}catch(Exception e){}
        }
        return false;
    }
    public Gson getGson(){
        return new GsonBuilder().create();
    }
}
