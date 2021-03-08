package io.github.kabirnayeem99.alarmforsalat.ui.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.media.VolumeShaper
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.PagerAdapter
import io.github.kabirnayeem99.alarmforsalat.databinding.ActivityAlarmForSalatBinding
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.MapsFragment
import org.osmdroid.config.Configuration

const val ACCESS_CODE_LOCATION = 1
const val ACCESS_CODE_STORAGE = 2

class AlarmForSalatActivity : AppCompatActivity() {
    private lateinit var fragmentAlarm: AlarmFragment
    lateinit var fragmentLocation: MapsFragment
    private lateinit var binding: ActivityAlarmForSalatBinding
    private var location: Location = Location("")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmForSalatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fixOpenStreetMapBug()
        initFragments()
        initTabLayout()
        requestLocationPermission()
    }

    private fun fixOpenStreetMapBug() {
        // fixed following this
        // https://github.com/osmdroid/osmdroid/issues/1313
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val context = applicationContext
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_CODE_LOCATION
            )
        }
    }

    private fun requestStorageLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                ACCESS_CODE_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_CODE_LOCATION -> when {
                grantResults[0] != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "You didn't grant this permission", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(this, "You granted this permission", Toast.LENGTH_SHORT)
                        .show()

                    getUserLocation()
                }
            }

            ACCESS_CODE_STORAGE -> when {
                grantResults[0] != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "You didn't grant location permission", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(this, "You granted location permission", Toast.LENGTH_SHORT)
                        .show()

                    getUserLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getUserLocation() {
        val userLocationListener = UserLocationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000,
                3f,
                userLocationListener
            )
            val l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            Toast.makeText(this, l?.latitude.toString(), Toast.LENGTH_SHORT).show()
            return
        }

        var userLocation = LatLng(location.latitude, location.longitude)
        Toast.makeText(this, "Your location is at $location", Toast.LENGTH_SHORT).show()

    }

    private fun initTabLayout() {
        with(binding) {
            pager.apply {
                adapter = PagerAdapter(supportFragmentManager, this@AlarmForSalatActivity)
            }
            tabs.setupWithViewPager(binding.pager)
            tabs.getTabAt(0)?.setIcon(R.drawable.ic_alarm_clock)
            tabs.getTabAt(1)?.setIcon(R.drawable.ic_location)
        }

    }

    private fun initFragments() {
        fragmentAlarm = AlarmFragment()
        fragmentLocation = MapsFragment()
    }


    inner class UserLocationListener : LocationListener {

        init {
            location.longitude = 0.0
            location.latitude = 0.0
        }

        override fun onLocationChanged(l: Location) {
            location = l
        }
    }


}