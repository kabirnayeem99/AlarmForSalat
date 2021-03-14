package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
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
import java.io.InputStream


class MapsFragment : Fragment() {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
   */
    private var _binding: FragmentMapsBinding? = null

    lateinit var cities: List<City>


    private val binding get() = _binding!!
    private lateinit var placesResponse: PlacesResponse

    private val callback = OnMapReadyCallback { googleMap ->
        val city = cities[0]
        val ctg = LatLng(city.lat, city.lng)
        googleMap.addMarker(
            MarkerOptions().position(ctg).title("Marker in ${cities[0].city}")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ctg, 14f))
    }

    private fun retrieveCities(): List<City> {

        val inputStream: InputStream = requireActivity().assets.open(Constants.placesFileName)

        val json: String? = Utilities.fileToJson(inputStream)

        if (json != null) {
            placesResponse = Gson().fromJson(json, PlacesResponse::class.java)
        } else {
            return listOf(City("", "", 0.0, 0.0, 20))
        }
        return placesResponse
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
        cities = retrieveCities()
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(callback)

//        mapFragment?.setMenuVisibility(false)

        val activityContext = requireContext()

        val preferences = ApplicationPreferences(activityContext)

        if (preferences.getCityName().isNotEmpty()) {
            binding.tvCurrentCity?.text = "Your current city is ${preferences.getCityName()}"
        }

        val placeAdapter = initPlaceAdapter(preferences)

        initRecyclerView(placeAdapter)
        setUpSearchListener(placeAdapter)


    }

    private fun initPlaceAdapter(
        preferences: ApplicationPreferences,
    ): PlaceAdapter {
        return PlaceAdapter { city ->
            preferences.setCity(city)

            binding.tvCurrentCity?.text = "Your current city is ${preferences.getCityName()}"
        }

    }

    private fun setUpSearchListener(placeAdapter: PlaceAdapter) {
        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    placeAdapter.filter.filter(s)
                } else {
                    placeAdapter.differ.submitList(cities)
                }
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

        placeAdapter.differ.submitList(cities)


    }

}