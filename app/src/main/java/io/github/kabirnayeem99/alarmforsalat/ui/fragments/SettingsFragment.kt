package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.utils.App
import io.github.kabirnayeem99.alarmforsalat.utils.ApplicationPreferencesRepository

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private const val TAG = "SettingsFragment"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.d(TAG, "onCreatePreferences: started")
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        Log.d(TAG, "onCreatePreferences: ${preferenceManager.sharedPreferencesName}")
        Log.d(
            TAG, "onCreatePreferences: ${
                ApplicationPreferencesRepository(App.context)
                    .getCityName()
            }"
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // fixes Fragments displayed over each other
        container?.removeAllViews()

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val madhabPreference =
            preferenceManager.findPreference<Preference>("madhhab") as ListPreference
        val adhanMethodPreference =
            preferenceManager.findPreference<Preference>("methods") as ListPreference


        madhabPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "onPreferenceChange: $newValue")
                true
            }


        adhanMethodPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                Log.d(TAG, "onPreferenceChange: $newValue")
                true
            }

    }
}