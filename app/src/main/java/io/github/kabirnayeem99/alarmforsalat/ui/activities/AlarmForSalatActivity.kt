package io.github.kabirnayeem99.alarmforsalat.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.PagerAdapter
import io.github.kabirnayeem99.alarmforsalat.data.LocationService
import io.github.kabirnayeem99.alarmforsalat.data.LocationService.LocationResult
import io.github.kabirnayeem99.alarmforsalat.databinding.ActivityAlarmForSalatBinding
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.MapsFragment
import io.github.kabirnayeem99.alarmforsalat.ui.viewmodels.AdhanViewModel
import io.github.kabirnayeem99.alarmforsalat.ui.viewmodels.AdhanViewModelFactory
import io.github.kabirnayeem99.alarmforsalat.utils.ApplicationPreferences


const val ACCESS_CODE_LOCATION = 1

class AlarmForSalatActivity : AppCompatActivity() {
    private lateinit var fragmentAlarm: AlarmFragment
    private lateinit var fragmentLocation: MapsFragment
    private lateinit var binding: ActivityAlarmForSalatBinding
    lateinit var viewModel: AdhanViewModel
    lateinit var preferences: ApplicationPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmForSalatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setUpPreferences()
        setUpViewModel()
        initFragments()
        initTabLayout()
        requestLocationPermission()
        getUserLocation()
    }

    private fun setUpPreferences() {
        preferences = ApplicationPreferences(this)
    }

    private fun setUpViewModel() {
        val repo = AdhanRepo()
        val viewModelFactory =
            AdhanViewModelFactory(repo, preferences.getCityName(), preferences.getCountryName())

        viewModel = ViewModelProvider(this, viewModelFactory).get(AdhanViewModel::class.java)
    }


    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                ACCESS_CODE_LOCATION
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
                grantResults[1] != PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(this, "You didn't grant this permission", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(this, "You granted all permissions", Toast.LENGTH_SHORT)
                        .show()

                    getUserLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getUserLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationResult: LocationResult = object : LocationResult() {
                override fun gotLocation(location: Location?) {
                    Log.d(
                        TAG,
                        "onCreate: lat -> ${location?.latitude}, long -> ${location?.longitude}"
                    )

                }
            }
            val locationService = LocationService()
            locationService.getLocation(this, locationResult)
        }
    }

    private fun initTabLayout() {
        with(binding) {
            pager.apply {
                adapter = PagerAdapter(supportFragmentManager, this@AlarmForSalatActivity)
            }
            tabs.setupWithViewPager(binding.pager)

            // adds icon to the tab title position
            tabs.getTabAt(0)?.setIcon(R.drawable.ic_alarm_clock)
            tabs.getTabAt(1)?.setIcon(R.drawable.ic_date)
            tabs.getTabAt(2)?.setIcon(R.drawable.ic_location)
        }

    }

    private fun initFragments() {
        fragmentAlarm = AlarmFragment()
        fragmentLocation = MapsFragment()
    }


    /*
    Options menu settings
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuSettingsMain -> {
                Toast.makeText(this, "You selected settings", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        private const val TAG = "AlarmForSalatActivity"
    }

}