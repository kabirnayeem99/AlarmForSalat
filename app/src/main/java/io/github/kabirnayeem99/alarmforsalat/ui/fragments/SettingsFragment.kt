package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.github.kabirnayeem99.alarmforsalat.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}