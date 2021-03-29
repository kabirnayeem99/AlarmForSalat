package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.SalatTimingsRecyclerViewAdapter
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentAlarmBinding
import io.github.kabirnayeem99.alarmforsalat.ui.activities.AlarmForSalatActivity
import io.github.kabirnayeem99.alarmforsalat.ui.viewmodels.AdhanViewModel
import io.github.kabirnayeem99.alarmforsalat.utils.AdhanTimeUtilities
import io.github.kabirnayeem99.alarmforsalat.utils.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Fragments that hold the Alarm Lists
 */
class AlarmFragment : Fragment(R.layout.fragment_alarm) {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
     for view binding
     */
    private var _binding: FragmentAlarmBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: AdhanViewModel
    private val salatTimingsRecyclerViewAdapter = SalatTimingsRecyclerViewAdapter()
    val settingsManager = SettingsManager.instance


    companion object {
        private const val TAG = "AlarmFragment"

        // creates a single instance of alarm fragment follows Singleton pattern
        val instance: AlarmFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { AlarmFragment() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)


        // as it doesn't load with each tab change
        setUpAlarm()

        return binding.root
    }

    private fun setUpAlarm() {

        if ((activity as AlarmForSalatActivity).placeChanged || (activity as AlarmForSalatActivity).appOnStart) {
            setAlarmDataToDb(settingsManager)
        }

        setAlarm(salatTimingsRecyclerViewAdapter)

        (activity as AlarmForSalatActivity).appOnStart = false

    }

    private fun setAlarmDataToDb(settingsManager: SettingsManager) {

        val viewModel = (activity as AlarmForSalatActivity).viewModel

        settingsManager.cityLatFlow.asLiveData().observe(viewLifecycleOwner, Observer { cityLat ->
            settingsManager.cityLongFlow.asLiveData()
                .observe(viewLifecycleOwner, { cityLong ->

                    CoroutineScope(Dispatchers.IO).launch {
                        val salats = AdhanTimeUtilities(
                            cityLat.toDouble(),
                            cityLong.toDouble()
                        ).getSalatTimingList()
                        for (salat in salats) {
                            viewModel.insert(salat)
                        }
                    }
                })
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as AlarmForSalatActivity).viewModel


        setUpRecyclerView(salatTimingsRecyclerViewAdapter)


    }

    private fun setAlarm(
        salatTimingsRecyclerViewAdapter: SalatTimingsRecyclerViewAdapter
    ) {

        Log.d(TAG, "setAlarm: set alarm was called ")
        viewModel = (activity as AlarmForSalatActivity).viewModel
        viewModel.getSalatTimings().asLiveData()
            .observe(viewLifecycleOwner, { salatTimingList ->
                salatTimingsRecyclerViewAdapter.differ.submitList(salatTimingList)

            })
//        with(settingsManager) {
//            cityLatFlow.asLiveData().observe(viewLifecycleOwner, { cityLat ->
//                cityLongFlow.asLiveData().observe(viewLifecycleOwner, { cityLong ->
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val salats = AdhanTimeUtilities(
//                            cityLat.toDouble(),
//                            cityLong.toDouble()
//                        ).getSalatTimingList()
//
////                        for ((index, salat) in salats.withIndex()) {
////                            Utilities.setUpAlarm(salat.time, index)
////                        }
//
//                        withContext(Dispatchers.Main) {
//                            salatTimingsRecyclerViewAdapter.differ.submitList(salats)
//
//                        }
//                    }
//                })
//
//            })
//        }
    }

    /**
     * Sets up the recycler view
     */
    private fun setUpRecyclerView(salatTimingsRecyclerViewAdapter: SalatTimingsRecyclerViewAdapter) {
        val rvFiveSalats = _binding?.rvFiveSalats
        rvFiveSalats?.let {
            it.adapter = salatTimingsRecyclerViewAdapter
            it.layoutManager = LinearLayoutManager(context)
        }
    }


//    private fun createObserver(salatTimingsRecyclerViewAdapter: SalatTimingsRecyclerViewAdapter) {
//
//        viewModel.adhanTime.observe(viewLifecycleOwner, { resources ->
//            when (resources) {
//                is Resource.Success -> {
//                    Log.d(TAG, "createObserver: ${resources.data}")
//                    resources.data?.data?.timings?.let {
//                        with(it) {
//                            DataHandler.setTimeInString(
//                                arrayListOf(Fajr, Dhuhr, Asr, Maghrib, Isha)
//                            ).also {
//                                salatTimingsRecyclerViewAdapter.differ.submitList(DataHandler.initialiseData())
//                            }
//                        }
//                    }
//                }
//
//                is Resource.Error -> {
//                    Log.d(TAG, "createObserver: ${resources.message}")
//                    resources.message?.let { message ->
//                        Toast.makeText(
//                            context,
//                            message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//                else -> {
//                    Log.d(TAG, "createObserver: loading")
//                    Toast.makeText(
//                        context,
//                        "Your Salat times are loading...",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        })
//    }
}