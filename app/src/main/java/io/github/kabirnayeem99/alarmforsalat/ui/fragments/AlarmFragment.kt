package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.SalatTimingsRecyclerViewAdapter
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentAlarmBinding


class AlarmFragment : Fragment(R.layout.fragment_alarm) {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
     for view binding
     */
    private var _binding: FragmentAlarmBinding? = null

    private val binding get() = _binding!!

    companion object {
        val instance: AlarmFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { AlarmFragment() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayList = initialiseSampleData()
        val salatTimingsRecyclerViewAdapter = SalatTimingsRecyclerViewAdapter()

        val rvFiveSalats = _binding?.rvFiveSalats
        rvFiveSalats?.let {
            it.adapter = salatTimingsRecyclerViewAdapter

            it.layoutManager = LinearLayoutManager(context)
        }

        salatTimingsRecyclerViewAdapter.differ.submitList(arrayList)

    }

    private fun initialiseSampleData(): ArrayList<SalatTiming> {
        val arrayList = arrayListOf<SalatTiming>()
        val fajr = SalatTiming(1, "Fajr", "5:00", true)
        arrayList.add(fajr)
        val dhuhr = SalatTiming(2, "Dhuhr", "5:00", false)
        arrayList.add(dhuhr)
        val asr = SalatTiming(3, "Asr", "5:00", true)
        arrayList.add(asr)
        val maghrib = SalatTiming(4, "Maghrib", "5:00", false)
        arrayList.add(maghrib)
        val isha = SalatTiming(5, "Isha", "5:00", true)
        arrayList.add(isha)
        return arrayList
    }
}