package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.SalatTimingsRecyclerViewAdapter
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentAlarmBinding
import io.github.kabirnayeem99.alarmforsalat.ui.activities.AlarmForSalatActivity
import io.github.kabirnayeem99.alarmforsalat.ui.viewmodels.AdhanViewModel
import io.github.kabirnayeem99.alarmforsalat.utils.AdhanTimeUtilities
import io.github.kabirnayeem99.alarmforsalat.utils.DataHandler
import io.github.kabirnayeem99.alarmforsalat.utils.Resource
import io.github.kabirnayeem99.alarmforsalat.utils.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
    val salatTimingsRecyclerViewAdapter = SalatTimingsRecyclerViewAdapter()


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


        val settingsManager = SettingsManager.instance

        if ((activity as AlarmForSalatActivity).placeChanged || (activity as AlarmForSalatActivity).appOnStart) {
            setAlarm(settingsManager, salatTimingsRecyclerViewAdapter)
            if ((activity as AlarmForSalatActivity).placeChanged) {
                Toast.makeText(context, "Place has changed", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onViewCreated: Place has changed")
            } else if ((activity as AlarmForSalatActivity).appOnStart) {
                Toast.makeText(context, "App has started", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onViewCreated: App has started")
            }
        }

        (activity as AlarmForSalatActivity).appOnStart = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as AlarmForSalatActivity).viewModel


        setUpRecyclerView(salatTimingsRecyclerViewAdapter)


    }

    private fun setAlarm(
        settingsManager: SettingsManager,
        salatTimingsRecyclerViewAdapter: SalatTimingsRecyclerViewAdapter
    ) {

        Log.d(TAG, "setAlarm: set alarm was called ")
        with(settingsManager) {
            cityLatFlow.asLiveData().observe(viewLifecycleOwner, { cityLat ->
                cityLongFlow.asLiveData().observe(viewLifecycleOwner, { cityLong ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val t = AdhanTimeUtilities(
                            cityLat.toDouble(),
                            cityLong.toDouble()
                        ).getSalatTimingList()
                        withContext(Dispatchers.Main) {
                            salatTimingsRecyclerViewAdapter.differ.submitList(t)
                        }
                    }
                })

            })
        }
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

    /**
     * Observes the AlAdhan Api for changes
     * and fills the recycler view based on that
     * @param salatTimingsRecyclerViewAdapter [SalatTimingsRecyclerViewAdapter]
     */
    private fun createObserver(salatTimingsRecyclerViewAdapter: SalatTimingsRecyclerViewAdapter) {

        viewModel.adhanTime.observe(viewLifecycleOwner, { resources ->
            when (resources) {
                is Resource.Success -> {
                    Log.d(TAG, "createObserver: ${resources.data}")
                    resources.data?.data?.timings?.let {
                        with(it) {
                            DataHandler.setTimeInString(
                                arrayListOf(Fajr, Dhuhr, Asr, Maghrib, Isha)
                            ).also {
                                salatTimingsRecyclerViewAdapter.differ.submitList(DataHandler.initialiseData())
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG, "createObserver: ${resources.message}")
                    resources.message?.let { message ->
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                else -> {
                    Log.d(TAG, "createObserver: loading")
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