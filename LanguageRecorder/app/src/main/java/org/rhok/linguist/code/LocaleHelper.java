package org.rhok.linguist.code;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * Created by lachlan on 20/06/2015.
 *
 * Contains code to change the applications locale
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
            if(Build.VERSION.SDK_INT>=17) {
                //not available before Android api 17
                config.setLocale(locale);
            }
            else{
                config.locale = locale;
            }
            baseContext.getResources().updateConfiguration(config, baseContext.getResources().getDisplayMetrics());

            return true;
        }

        return false;
    }
}
