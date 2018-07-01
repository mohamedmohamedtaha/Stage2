package com.example.manasatpc.stage1;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// this class of set Preference user
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public static class StoryPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference section = findPreference(getString(R.string.settings_department_key));
            bindPreferenceSummaryToValue(section);

            Preference from_date = findPreference(getString(R.string.settings_from_date_key));
            bindPreferenceSummaryToValue(from_date);

            Preference to_date = findPreference(getString(R.string.settings_to_date_key));
            bindPreferenceSummaryToValue(to_date);

            Preference order_date = findPreference(getString(R.string.settings_order_date_key));
            bindPreferenceSummaryToValue(order_date);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            // The code in this method takes care of updating the displayed preference summary after it has been changed
            String stringValue = o.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);

            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());

            String prefString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, prefString);
        }
    }
}
























