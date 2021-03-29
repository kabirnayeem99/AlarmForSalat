package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.PlaceAdapter
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentMapsBinding
import io.github.kabirnayeem99.alarmforsalat.ui.activities.AlarmForSalatActivity
import io.github.kabirnayeem99.alarmforsalat.utils.App
import io.github.kabirnayeem99.alarmforsalat.utils.ApplicationPreferencesRepository
import io.github.kabirnayeem99.alarmforsalat.utils.DataHandler
import io.github.kabirnayeem99.alarmforsalat.utils.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * This fragment shows a list of places to select from
 * In tablet and landscape shows a map beside the list
 */
class MapsFragment : Fragment() {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
   */


    private var _binding: FragmentMapsBinding? = null
    private val preferences = ApplicationPreferencesRepository(App.context)
    private val settingsManager = SettingsManager.instance


    private val binding get() = _binding!!


    companion object {
        val instance: MapsFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { MapsFragment() }
    }


    /**
     * With the loading of the map
     * it sets the marker
     * zooms in to the user selected place
     */
    private val callback = OnMapReadyCallback { googleMap ->

        // sets current location from the data gotten from the data store
        settingsManager.cityNameFlow.asLiveData().observe(viewLifecycleOwner, Observer {
            //todo: will add code to listen to changes
        })

        val currentLoc: LatLng =
            if (preferences.getCityName().trim().isNotEmpty()) {
                LatLng(
                    preferences.getLatitude(),
                    preferences.getLongtitude(),
                )
            } else {
                LatLng(0.0, 0.0)
            }

        // adds a marker to the map
        googleMap.addMarker(
            MarkerOptions().position(currentLoc)
                .title("Marker in ${preferences.getCityName()}")
        )

        // zooms the camera to the specified location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        mapFragment?.setMenuVisibility(false)


        val placeAdapter = initPlaceAdapter()

        CoroutineScope(Dispatchers.IO).launch {
            val citiesTempIn = DataHandler.getPlacesInCityList()
            withContext(Dispatchers.Main) {
                placeAdapter.differ.submitList(citiesTempIn)
            }
        }

        initRecyclerView(placeAdapter)
        setUpSearchListener(placeAdapter)
        setUpPlaceChangeTextChange()


    }

    /**
     * Based on the change in selected place, it changes the text view
     */
    private fun setUpPlaceChangeTextChange() {
        settingsManager.cityNameFlow.asLiveData().observe(viewLifecycleOwner, {
            binding.tvCurrentCity.text =
                "Your current city is $it"
        })
    }

    private fun initPlaceAdapter(): PlaceAdapter {
        return PlaceAdapter { city ->
            lifecycleScope.launch {
                settingsManager.setCity(city)
            }
            (activity as AlarmForSalatActivity).placeChanged = true
        }
    }

    /**
     * This function listens to the change in text filed and passes it to place adapter
     */
    private fun setUpSearchListener(placeAdapter: PlaceAdapter) {
        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                placeAdapter.filter.filter(s)
            }


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun initRecyclerView(placeAdapter: PlaceAdapter) {

        binding.rvPlaces.apply {
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

}