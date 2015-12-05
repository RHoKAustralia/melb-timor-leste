package org.rhok.linguist.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import org.rhok.linguist.R;

import java.util.Locale;

/**
 * Created by lachlan on 19/06/2015.
 *
 * This is the global application variable, it takes care of setting the applications
 * locale based on the settings file
 */
public class LinguistApplication extends Application {

    public static final boolean DEBUG = true;
    private Locale locale = null;
    private static LinguistApplication instance;
    @Override
    public void onCreate()
    {
        super.onCreate();
        instance=this;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        Configuration config = getBaseContext().getResources().getConfiguration();

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
