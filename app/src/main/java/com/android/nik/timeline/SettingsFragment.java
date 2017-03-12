package com.android.nik.timeline;


import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Фрагмент настроек приложения синхронизация и т.п.
 */
public class SettingsFragment extends PreferenceFragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }

}
