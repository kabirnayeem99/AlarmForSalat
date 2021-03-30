package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
 * Fragment that hold the Alarm Lists
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
    private val settingsManager = SettingsManager.instance


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as AlarmForSalatActivity).viewModel
        // as it doesn't load with each tab change
        setUpAlarm(viewModel)
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
        setUpRecyclerView(salatTimingsRecyclerViewAdapter)
    }

    /**
     * Sets up alarm, as it checks for place changes and only then
     * saves new alarm to the database, which significantly reduces cpu usage
     * @param viewModel [AdhanViewModel]
     */
    private fun setUpAlarm(viewModel: AdhanViewModel) {

        if ((activity as AlarmForSalatActivity).placeChanged) {
            saveAlarmDataToDb(settingsManager, viewModel)
        }

        setAlarmToRecyclerView(salatTimingsRecyclerViewAdapter)

        (activity as AlarmForSalatActivity).appOnStart = false

    }

    /**
     * Saves newly found alarm data to the database
     * Checks for location changes and gives location data based on that.
     * @param settings [SettingsManager]
     * @param viewModel [AdhanViewModel]
     */
    private fun saveAlarmDataToDb(settings: SettingsManager, viewModel: AdhanViewModel) {

        settings.cityLatFlow.asLiveData().observe(viewLifecycleOwner, { lat ->
            settings.cityLongFlow.asLiveData()
                .observe(viewLifecycleOwner, { long ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val salats = AdhanTimeUtilities(
                            lat.toDouble(),
                            long.toDouble()
                        ).getSalatTimingList()
                        for (salat in salats) {
                            viewModel.insert(salat)
                        }
                    }
                })
        })
    }


    /**
     * Sets alarm lists to recycler view
     * @param adapter [SalatTimingsRecyclerViewAdapter]
     */
    private fun setAlarmToRecyclerView(adapter: SalatTimingsRecyclerViewAdapter) {
        viewModel = (activity as AlarmForSalatActivity).viewModel
        viewModel.getSalatTimings().asLiveData()
            .observe(viewLifecycleOwner, { salatTimingList ->
                adapter.differ.submitList(salatTimingList)

            })
    }

    /**
     * Sets up the recycler view
     */
    private fun setUpRecyclerView(adapter: SalatTimingsRecyclerViewAdapter) {
        val rvFiveSalats = _binding?.rvFiveSalats
        rvFiveSalats?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        private const val TAG = "AlarmFragment"

        // creates a single instance of alarm fragment follows Singleton pattern
        val instance: AlarmFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { AlarmFragment() }
    }
}