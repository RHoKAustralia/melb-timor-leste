package org.rhok.linguist.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by lachlan on 20/06/2015.
 */
public class LocaleHelper {
    public static Boolean updateLocale(Context baseContext, Context thisContext) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(thisContext);
        Configuration config = baseContext.getResources().getConfiguration();
        Boolean tetumEnabled = settings.getBoolean("pref_tetum", false);
        String localeName = (tetumEnabled ? "tet" : "en");

        if (!config.locale.getLanguage().equals(localeName))
        {
            Locale locale = new Locale(localeName);
            Locale.setDefault(locale);

            config.setLocale(locale);
//            config.locale = locale;
            baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());

            return true;
        }

        return false;
    }
}
