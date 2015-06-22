package org.rhok.linguist.activity.common;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import org.rhok.linguist.R;

import java.util.Locale;

/**
 * Created by lachlan on 17/06/2015.
 *
 * Settings file.
 */

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

        // this is called when the user changes any of their preferences
        // at the moment we change the user's locale in BaseInterviewActivity
        // instead of here

        if (key.equals("pref_tetum")) {

        // Set summary to be the user-description for the selected value
        // connectionPref.setSummary(sharedPreferences.getString(key, ""));

        }
    }
}
