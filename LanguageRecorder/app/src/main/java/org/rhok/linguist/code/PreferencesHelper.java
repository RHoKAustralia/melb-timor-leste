package org.rhok.linguist.code;

import android.content.Context;
import android.provider.Settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.servicestack.client.JsonSerializers;

import org.rhok.linguist.api.models.Interviewer;
import org.rhok.linguist.application.LinguistApplication;
import org.rhok.linguist.network.PCJsonSerializers;

import java.util.Date;
import java.util.UUID;

/**
 * Created by bramleyt on 11/03/2017.
 */

public class PreferencesHelper {
    public static final String PREFS_FILE_APPPREFS = "LinguistAppPrefs";
    public static final String APPPREFS_KEY_INSTALLATION_ID = "installationID";

    public static final String PREFS_FILE_USERPREFS = "LinguistUserPrefs";
    public static final String USERPREFS_KEY_INTERVIEWER = "interviewer";

    public static Interviewer createDefaultInterviewer(){
        Interviewer interviewer = new Interviewer();
        String deviceId = getInstallationID();
        interviewer.setDevice_id(deviceId);
        interviewer.setName("Android User "+deviceId.replace("-","").substring(0,8));
        return interviewer;
    }
    public static Interviewer getInterviewer(){
        String json = getContext()
                .getSharedPreferences(PREFS_FILE_USERPREFS,0).getString(USERPREFS_KEY_INTERVIEWER,null);
        if(json==null) return null;
        return getGson().fromJson(json, Interviewer.class);
    }

    public static void saveInterviewer(Interviewer interviewer){
        String json = interviewer==null?null:getGson().toJson(interviewer);
        getContext().getSharedPreferences(PREFS_FILE_USERPREFS,0)
                .edit()
                .putString(USERPREFS_KEY_INTERVIEWER, json)
                .commit();
    }

    public static String getInstallationID(){
        return getContext().getSharedPreferences(PREFS_FILE_APPPREFS,0).getString(APPPREFS_KEY_INSTALLATION_ID,null);
    }
    private static Gson getGson(){
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, JsonSerializers.getDateSerializer())
                .registerTypeAdapter(Date.class, PCJsonSerializers.getDateDeserializer())
                //.setExclusionStrategies(PCJsonSerializers.getUnderscoreExclusionStrategy())
                .create();
    }
    private static Context getContext() {
        return LinguistApplication.getContextStatic();
    }
}
