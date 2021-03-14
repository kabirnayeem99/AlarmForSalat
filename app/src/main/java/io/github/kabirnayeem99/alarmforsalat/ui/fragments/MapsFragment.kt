package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.Utils.Constants
import io.github.kabirnayeem99.alarmforsalat.Utils.Utilities
import io.github.kabirnayeem99.alarmforsalat.adapters.PlaceAdapter
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.PlacesResponse
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentMapsBinding
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
    val placeAdapter = PlaceAdapter()
    { city ->
        Toast.makeText(
            requireContext(),
            "You have selected ${city.city} as your city",
            Toast.LENGTH_SHORT
        ).show()

    }


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

    companion object {
        val instance: MapsFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { MapsFragment() }
        private const val TAG = "MapsFragment"
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

        initRecyclerView()
        setUpSearchListener()


    }

    private fun setUpSearchListener() {
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

    private fun initRecyclerView() {

        binding.rvPlaces.apply {
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(context)
        }

        placeAdapter.differ.submitList(cities)


    }

}