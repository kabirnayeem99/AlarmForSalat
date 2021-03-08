package io.github.kabirnayeem99.alarmforsalat.ui.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.databinding.FragmentLocationBinding
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

class LocationFragment : Fragment(R.layout.fragment_location) {

    /*
    follows https://developer.android.com/topic/libraries/view-binding
    for view binding
    */
    lateinit var maps: MapView
    private var _binding: FragmentLocationBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maps = binding.maps
        maps.setTileSource(TileSourceFactory.MAPNIK);
        maps.setBuiltInZoomControls(true);
        maps.setMultiTouchControls(true);
    }

}