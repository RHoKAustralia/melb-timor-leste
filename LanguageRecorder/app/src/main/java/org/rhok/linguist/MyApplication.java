package org.rhok.linguist;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by lachlan on 19/06/2015.
 */
public class MyApplication extends Application {

    private Locale locale = null;


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


    @Override
    public void onCreate()
    {
        super.onCreate();

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
}
