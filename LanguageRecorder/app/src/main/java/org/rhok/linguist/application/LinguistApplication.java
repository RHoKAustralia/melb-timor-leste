package org.rhok.linguist.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import org.rhok.linguist.R;
import org.rhok.linguist.code.DiskSpace;
import org.rhok.linguist.code.PreferencesHelper;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by lachlan on 19/06/2015.
 *
 * This is the global application variable, it takes care of setting the applications
 * locale based on the settings file
 */
public class LinguistApplication extends Application {

    private static final String TAG = "App";

    public static final boolean DEBUG = true;
    private Locale locale = null;
    private static LinguistApplication instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance=this;
        SharedPreferences appPrefs = getSharedPreferences(PreferencesHelper.PREFS_FILE_APPPREFS, 0);
        if(!appPrefs.contains(PreferencesHelper.APPPREFS_KEY_INSTALLATION_ID)){
            appPrefs.edit().putString(PreferencesHelper.APPPREFS_KEY_INSTALLATION_ID, UUID.randomUUID().toString()).commit();
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        Configuration config = getBaseContext().getResources().getConfiguration();

        if (DiskSpace.createAppDirs()) Log.e(TAG, "Failed to create application directories");

        Boolean tetumEnabled = settings.getBoolean("pref_tetum", false);

        String localeName = (tetumEnabled ? "tet" : "en");

        if (!"".equals(localeName) && ! config.locale.getLanguage().equals(localeName))
        {
            locale = new Locale(localeName);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

    }

    public static Context getContextStatic(){
        return instance.getApplicationContext();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public static String getWebserviceUrl() {
        return instance.getString(R.string.server_prod);
    }
}
