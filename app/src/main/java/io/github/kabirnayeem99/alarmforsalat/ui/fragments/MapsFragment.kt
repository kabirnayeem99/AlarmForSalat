package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentAlarmBinding
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
   */
    private var _binding: FragmentMapsBinding? = null

    private val binding get() = _binding!!

    private val callback = OnMapReadyCallback { googleMap ->
        val ctg = LatLng(22.341685874789473, 91.7815618476437)
        googleMap.addMarker(MarkerOptions().position(ctg).title("Marker in ctg"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ctg, 14f))
    }

    companion object {
        val instance: MapsFragment by lazy(LazyThreadSafetyMode.PUBLICATION) { MapsFragment() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}