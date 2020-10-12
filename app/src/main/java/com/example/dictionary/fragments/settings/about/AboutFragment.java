package com.example.dictionary.fragments.settings.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.dictionary.BuildConfig;
import com.example.dictionary.R;
import com.example.dictionary.data.licence.LicencesUsed;
import com.example.dictionary.fragments.settings.about.licences.licence.LicenceFragment;


public class AboutFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.about_preferences,rootKey);

        Preference preferenceVersion = findPreference(getString(R.string.settings_about_version_key));
        if(preferenceVersion != null)
        {
            preferenceVersion.setSummary(BuildConfig.VERSION_NAME);
        }

        Preference preferenceSourceCode = findPreference(getString(R.string.settings_about_source_code_key));
        if(preferenceSourceCode != null)
        {
            preferenceSourceCode.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(preference.getSummary().toString()));
                startActivity(intent);
                return false;
            });
        }

        Preference preferenceLicence = findPreference(getString(R.string.settings_about_licence_key));
        if(preferenceLicence != null)
        {
            preferenceLicence.setSummary(LicencesUsed.getMITLicence().getName());
            preferenceLicence.setOnPreferenceClickListener(preference -> {
                Bundle bundle = new Bundle();
                bundle.putString(LicenceFragment.TITLE,LicencesUsed.getMITLicence().getName());
                bundle.putString(LicenceFragment.BODY, LicencesUsed.getMITLicence("2020","Martin Bilka").getTerms());

                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.navigation_licence_licences_about_settings, bundle);
                return false;
            });
        }

        Preference preferenceLicences = findPreference(getString(R.string.settings_about_licences_key));
        if(preferenceLicences != null)
        {
            preferenceLicences.setOnPreferenceClickListener(preference -> {
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.navigation_licences_about_settings);
                return false;
            });
        }

    }


}
