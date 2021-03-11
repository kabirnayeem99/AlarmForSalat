package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.Utils.Constants
import io.github.kabirnayeem99.alarmforsalat.Utils.Utilities
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.City
import io.github.kabirnayeem99.alarmforsalat.data.view_objects.PlacesResponse
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentMapsBinding
import java.io.InputStream
import java.nio.charset.Charset

class MapsFragment : Fragment() {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
   */
    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!
    lateinit var placesResponse: PlacesResponse

    private val callback = OnMapReadyCallback { googleMap ->
        val cities = retrieveCities()
        val city = cities[0]
        val ctg = LatLng(city.lat, city.lng)
        googleMap.addMarker(
            MarkerOptions().position(ctg).title("Marker in ${placesResponse[0].city}")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ctg, 14f))
    }

    private fun retrieveCities(): List<City> {

        val inputStream: InputStream = requireActivity().assets.open(Constants.placesFileName)

        val json: String? = Utilities.fileToJson(inputStream)

        if (json != null) {
            placesResponse = Gson().fromJson(json, PlacesResponse::class.java)
            Toast.makeText(requireContext(), placesResponse[0].city, Toast.LENGTH_SHORT).show()
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}