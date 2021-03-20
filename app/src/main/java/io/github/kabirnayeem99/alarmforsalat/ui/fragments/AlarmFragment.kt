package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.SalatTimingsRecyclerViewAdapter
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.SalatTiming
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.Time
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentAlarmBinding
import io.github.kabirnayeem99.alarmforsalat.service.alarm.AlarmService
import io.github.kabirnayeem99.alarmforsalat.ui.activities.AlarmForSalatActivity
import io.github.kabirnayeem99.alarmforsalat.ui.viewmodels.AdhanViewModel
import io.github.kabirnayeem99.alarmforsalat.utils.DataHandler
import io.github.kabirnayeem99.alarmforsalat.utils.Resource
import io.github.kabirnayeem99.alarmforsalat.utils.Utilities
import java.util.*
import kotlin.collections.ArrayList


class AlarmFragment : Fragment(R.layout.fragment_alarm) {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
     for view binding
     */
    private var _binding: FragmentAlarmBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: AdhanViewModel

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

        viewModel = (activity as AlarmForSalatActivity).viewModel
        val salatTimingsRecyclerViewAdapter = SalatTimingsRecyclerViewAdapter()

        createObserver(salatTimingsRecyclerViewAdapter)

        setUpRecyclerView(salatTimingsRecyclerViewAdapter)
        val alarmService = context?.let { AlarmService(it) }

        val c = Calendar.getInstance()
        alarmService?.setExactAlarm(c.timeInMillis)
    }

    private fun setUpRecyclerView(salatTimingsRecyclerViewAdapter: SalatTimingsRecyclerViewAdapter) {
        val rvFiveSalats = _binding?.rvFiveSalats
        rvFiveSalats?.let {
            it.adapter = salatTimingsRecyclerViewAdapter

            it.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun createObserver(salatTimingsRecyclerViewAdapter: SalatTimingsRecyclerViewAdapter) {
        viewModel.adhanTime.observe(viewLifecycleOwner, { resources ->
            when (resources) {
                is Resource.Success -> {
                    resources.data?.data?.timings?.let {
                        with(it) {
                            DataHandler.setTimeInString(
                                arrayListOf(
                                    Fajr,
                                    Dhuhr,
                                    Asr,
                                    Maghrib,
                                    Isha
                                )
                            ).also {
                                salatTimingsRecyclerViewAdapter.differ.submitList(DataHandler.initialiseData())
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    resources.message?.let { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else -> {
                    Toast.makeText(
                        context,
                        "Your Salat times are loading...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }


}