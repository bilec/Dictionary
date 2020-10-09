package com.example.dictionary.fragments.settings.dictionary;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.dictionary.R;

public class DictionaryFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.dictionary_preferences, rootKey);

        ListPreference searchPattern = findPreference(getString(R.string.settings_search_pattern_key));
        if(searchPattern != null) {
            bindListPreferenceSummaryToValue(searchPattern);
        }

        Preference invert = findPreference(getString(R.string.settings_invert_key));
        if(invert != null) {
            invert.setOnPreferenceChangeListener((preference, newValue) -> {

                CheckBoxPreference kssjDic = findPreference(getString(R.string.settings_dictionary_kssj_value));
                CheckBoxPreference pspDic = findPreference(getString(R.string.settings_dictionary_psp_value));
                CheckBoxPreference sssjDic = findPreference(getString(R.string.settings_dictionary_sssj_value));
                CheckBoxPreference orterDic = findPreference(getString(R.string.settings_dictionary_orter_value));
                CheckBoxPreference scsDic = findPreference(getString(R.string.settings_dictionary_scs_value));
                CheckBoxPreference sssDic = findPreference(getString(R.string.settings_dictionary_sss_value));
                CheckBoxPreference peciarDic = findPreference(getString(R.string.settings_dictionary_peciar_value));

                CheckBoxPreference hssjVDic = findPreference(getString(R.string.settings_dictionary_hssjV_value));
                CheckBoxPreference bernolakDic = findPreference(getString(R.string.settings_dictionary_bernolak_value));

                CheckBoxPreference noundbDic = findPreference(getString(R.string.settings_dictionary_noundb_value));
                CheckBoxPreference orientDic = findPreference(getString(R.string.settings_dictionary_orient_value));
                CheckBoxPreference locutioDic = findPreference(getString(R.string.settings_dictionary_locutio_value));
                CheckBoxPreference obceDic = findPreference(getString(R.string.settings_dictionary_obce_value));
                CheckBoxPreference priezviskaDic = findPreference(getString(R.string.settings_dictionary_priezviska_value));
                CheckBoxPreference unDic = findPreference(getString(R.string.settings_dictionary_un_value));
                CheckBoxPreference pskcsDic = findPreference(getString(R.string.settings_dictionary_pskcs_value));
                CheckBoxPreference pskenDic = findPreference(getString(R.string.settings_dictionary_psken_value));


                if (kssjDic != null) kssjDic.setChecked(!kssjDic.isChecked());
                if (pspDic != null) pspDic.setChecked(!pspDic.isChecked());
                if (sssjDic != null) sssjDic.setChecked(!sssjDic.isChecked());
                if (orterDic != null) orterDic.setChecked(!orterDic.isChecked());
                if (scsDic != null) scsDic.setChecked(!scsDic.isChecked());
                if (sssDic != null) sssDic.setChecked(!sssDic.isChecked());
                if (peciarDic != null) peciarDic.setChecked(!peciarDic.isChecked());

                if (hssjVDic != null) hssjVDic.setChecked(!hssjVDic.isChecked());
                if (bernolakDic != null) bernolakDic.setChecked(!bernolakDic.isChecked());

                if (noundbDic != null) noundbDic.setChecked(!noundbDic.isChecked());
                if (orientDic != null) orientDic.setChecked(!orientDic.isChecked());
                if (locutioDic != null) locutioDic.setChecked(!locutioDic.isChecked());
                if (obceDic != null) obceDic.setChecked(!obceDic.isChecked());
                if (priezviskaDic != null) priezviskaDic.setChecked(!priezviskaDic.isChecked());
                if (unDic != null) unDic.setChecked(!unDic.isChecked());
                if (pskcsDic != null) pskcsDic.setChecked(!pskcsDic.isChecked());
                if (pskenDic != null) pskenDic.setChecked(!pskenDic.isChecked());

                return true;
            });
        }
    }

    private void bindListPreferenceSummaryToValue(ListPreference preference)
    {
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        String preferenceString = sharedPreferences.getString(preference.getKey(), "");
        if(preferenceString != null)
        {
            onPreferenceChange(preference,preferenceString);
        }
        else
        {
            onPreferenceChange(preference,"");
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if(preference instanceof ListPreference)
        {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if(prefIndex >= 0)
            {
                CharSequence[] labels = listPreference.getEntries();
                preference.setSummary(labels[prefIndex]);
            }
        }

        return true;
    }

}