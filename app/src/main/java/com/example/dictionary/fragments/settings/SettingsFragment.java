package com.example.dictionary.fragments.settings;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.dictionary.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.root_preferences,rootKey);

        Preference preferenceDictionary = findPreference(getString(R.string.settings_dictionary_key));
        if(preferenceDictionary != null)
        {
            preferenceDictionary.setOnPreferenceClickListener(preference -> navigate(R.id.navigation_dictionary_settings));
        }

        Preference preferenceAppearance = findPreference(getString(R.string.settings_appearance_key));
        if(preferenceAppearance != null)
        {
            preferenceAppearance.setOnPreferenceClickListener(preference -> navigate(R.id.navigation_appearance_settings));
        }

        Preference preferenceAbout = findPreference(getString(R.string.settings_about_key));
        if(preferenceAbout != null)
        {
            preferenceAbout.setOnPreferenceClickListener(preference -> navigate(R.id.navigation_about_settings));
        }

    }

    private boolean navigate(int id)
    {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(id);
        return true;
    }

}
