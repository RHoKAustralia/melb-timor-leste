package org.rhok.linguist;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import java.util.Locale;


public class AppSettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals("pref_tetum")) {
            //Preference connectionPref = findPreference(key);
            /*Boolean tetumEnabled = sharedPreferences.getBoolean("pref_tetum", false);

            String localeName = (tetumEnabled ? "tet" : "en");

            Locale newLocale = new Locale(localeName);
            Locale.setDefault(newLocale);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.setLocale(newLocale);*/

        // Set summary to be the user-description for the selected value
  //          connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }
}
