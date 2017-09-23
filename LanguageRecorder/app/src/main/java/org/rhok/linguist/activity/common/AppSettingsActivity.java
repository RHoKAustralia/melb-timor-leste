package org.rhok.linguist.activity.common;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.os.Bundle;

import org.rhok.linguist.R;

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
    }
}
