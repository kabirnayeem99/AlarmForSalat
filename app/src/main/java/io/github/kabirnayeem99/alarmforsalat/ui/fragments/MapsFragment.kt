package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.provider.Contacts
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.utils.Constants
import io.github.kabirnayeem99.alarmforsalat.utils.Utilities
import io.github.kabirnayeem99.alarmforsalat.adapters.PlaceAdapter
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.PlacesResponse
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentMapsBinding
import io.github.kabirnayeem99.alarmforsalat.utils.ApplicationPreferences
import io.github.kabirnayeem99.alarmforsalat.utils.DataHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


class MapsFragment : Fragment() {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
   */
    private var _binding: FragmentMapsBinding? = null

    lateinit var cities: List<City>
    private var currentCityLatLang: MutableLiveData<List<Double>> = MutableLiveData()
    var currentCityName: MutableLiveData<String> = MutableLiveData()


    private val binding get() = _binding!!
    private lateinit var placesResponse: PlacesResponse

    private val callback = OnMapReadyCallback { googleMap ->

        currentCityLatLang.observe(viewLifecycleOwner, { cityLatLang ->
            val currentLoc = LatLng(cityLatLang[0], cityLatLang[1])

            currentCityName.observe(viewLifecycleOwner, { cityName ->
                googleMap.addMarker(
                    MarkerOptions().position(currentLoc).title("Marker in $cityName")
                )
            })

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14f))
        })

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
        cities = listOf<City>()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        mapFragment?.setMenuVisibility(false)

        val activityContext = requireContext()

        val preferences = ApplicationPreferences(activityContext)

        val currentCityLat = preferences.getLat()
        val currentCityLong = preferences.getLong()
        currentCityLatLang.postValue(arrayListOf(currentCityLat, currentCityLong))



        if (preferences.getCityName().isNotEmpty()) {
            currentCityName.postValue(preferences.getCityName())
            binding.tvCurrentCity.text = "Your current city is ${preferences.getCityName()}"
        }

        val placeAdapter = initPlaceAdapter(preferences)

        CoroutineScope(Dispatchers.IO).launch {
            val citiesTempIn = DataHandler.getPlacesInCityList()
            withContext(Dispatchers.Main) {
                placeAdapter.differ.submitList(citiesTempIn)
            }
        }


        initRecyclerView(placeAdapter)
        setUpSearchListener(placeAdapter)


    }

    private fun initPlaceAdapter(
        preferences: ApplicationPreferences,
    ): PlaceAdapter {
        return PlaceAdapter { city ->
            preferences.setLocation(city)

            binding.tvCurrentCity.text = "Your current city is ${preferences.getCityName()}"
        }

    }

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