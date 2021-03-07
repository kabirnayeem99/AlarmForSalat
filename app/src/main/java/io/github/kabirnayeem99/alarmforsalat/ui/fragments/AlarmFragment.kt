package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.github.kabirnayeem99.alarmforsalat.R


class AlarmFragment() : Fragment(R.layout.fragment_alarm) {

    companion object {
        val instance: AlarmFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { AlarmFragment() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        

    }
}