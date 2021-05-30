package com.example.scantosheet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {

    public static final String PREF_SS_ID = "pref_ss_id";
    public static final String PREF_USERNAME = "pref_username";
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                int f;

                f = 0;
                if(key.equals(PREF_SS_ID)) {
                    Preference ssIdPref = findPreference(key);
                    String ssId = sharedPreferences.getString(key, "");

                    if (ssId.indexOf("spreadsheets/d/") > 0) {
                        ssId = ssId.substring(ssId.indexOf("spreadsheets/d/") + 15);
                        f++;
                    }
                    if (ssId.indexOf("/edit") > 0) {
                        ssId = ssId.substring(0, ssId.indexOf("/edit"));
                        f++;
                    }
                    if (f > 0) {
                        getPreferenceScreen().getSharedPreferences().edit().putString(PREF_SS_ID, ssId).apply();
                    }

                    ssIdPref.setSummary(ssId);
                }
                if(key.equals(PREF_USERNAME)) {
                    Preference usernamePref = findPreference(key);
                    usernamePref.setSummary(sharedPreferences.getString(key, ""));
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        int f;

        f = 0;
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        Preference ssIdPref = findPreference(PREF_SS_ID);
        String ssId = getPreferenceScreen().getSharedPreferences().getString(PREF_SS_ID, "");
        if (ssId.indexOf("spreadsheets/d/") > 0) {
            ssId = ssId.substring(ssId.indexOf("spreadsheets/d/") + 15);
            f++;
        }
        if (ssId.indexOf("/edit") > 0) {
            ssId = ssId.substring(0, ssId.indexOf("/edit"));
            f++;
        }
        if (f > 0) {
            getPreferenceScreen().getSharedPreferences().edit().putString(PREF_SS_ID, ssId).apply();
        }
        ssIdPref.setSummary(ssId);


        Preference usernamePref = findPreference(PREF_USERNAME);
        usernamePref.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_USERNAME, ""));
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferencesChangeListener);
    }
}
