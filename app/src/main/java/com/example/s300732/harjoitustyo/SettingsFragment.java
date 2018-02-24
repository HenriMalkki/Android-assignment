package com.example.s300732.harjoitustyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String key_interval = "interval_preference";
    public static final String key_distance = "distance_preference";

    SharedPreferences pref;
    SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        pref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        mEditor = pref.edit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {

        if (key.equals(key_interval))
        {
            // Set summary to be the user-description for the selected value

            Preference intervalPref = findPreference(key);
            intervalPref.setSummary(sharedPreferences.getString(key, ""));
            mEditor.putString("interval", intervalPref.getSummary().toString());
            mEditor.commit();
            Log.d("Taalla ollaan", "" + intervalPref.getSummary());
        }
        else if (key.equals(key_distance))
        {
            // Set summary to be the user-description for the selected value
            Preference distancePref = findPreference(key);
            distancePref.setSummary(sharedPreferences.getString(key, ""));
            mEditor.putString("distance", distancePref.getSummary().toString());
            mEditor.commit();
            Log.d("Taalla ollaan", "" + distancePref.getSummary());
        }
    }

}
