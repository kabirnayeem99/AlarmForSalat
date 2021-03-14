package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentAlarmBinding
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentDateBinding

class DateFragment : Fragment() {
    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
  */
    private var _binding: FragmentDateBinding? = null

    private val binding get() = _binding!!

    companion object {
        val instance: DateFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { DateFragment() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDateBinding.inflate(inflater, container, false)
        return binding.root
    }

}