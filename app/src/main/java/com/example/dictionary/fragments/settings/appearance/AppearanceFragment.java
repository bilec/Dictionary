package com.example.dictionary.fragments.settings.appearance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.dictionary.R;

public class AppearanceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.appearance_preferences, rootKey);

        ListPreference nightMode = findPreference(getString(R.string.settings_night_mode_key));
        if(nightMode != null)
        {
            nightMode.setSummary(nightMode.getEntry());
            nightMode.setOnPreferenceChangeListener(this);
        }

        ListPreference fontSize = findPreference(getString(R.string.settings_font_size_key));
        if(fontSize != null)
        {
            fontSize.setSummary(fontSize.getEntry());
            fontSize.setOnPreferenceChangeListener(this);
        }

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if(prefIndex >= 0)
            {
                CharSequence[] labels = listPreference.getEntries();
                preference.setSummary(labels[prefIndex]);
            }


            String key = listPreference.getEntryValues()[prefIndex].toString();

            actionNightMode(key);

            actionFontSize(key);

        }

        return true;
    }

    private void actionNightMode(String key)
    {
        if(key.equals(getString(R.string.settings_night_mode_disabled_value)))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(key.equals(getString(R.string.settings_night_mode_always_on_value)))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else if(key.equals(getString(R.string.settings_night_mode_automatic_value)))
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
        }
    }

    private void actionFontSize(String key)
    {
        boolean recreate = false;

        if(key.equals(getString(R.string.settings_font_size_small_value)))
        {
            recreate = true;
        }
        else if(key.equals(getString(R.string.settings_font_size_normal_value)))
        {
            recreate = true;
        }
        else if(key.equals(getString(R.string.settings_font_size_large_value)))
        {
            recreate = true;
        }
        else if(key.equals(getString(R.string.settings_font_size_larger_value)))
        {
            recreate = true;
        }

        if(recreate)
        {
            requireActivity().recreate();
        }
    }

}
