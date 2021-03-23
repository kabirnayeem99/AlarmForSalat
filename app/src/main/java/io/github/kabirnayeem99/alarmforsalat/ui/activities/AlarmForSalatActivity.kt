package io.github.kabirnayeem99.alarmforsalat.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.kabirnayeem99.alarmforsalat.R
import io.github.kabirnayeem99.alarmforsalat.adapters.PagerAdapter
import io.github.kabirnayeem99.alarmforsalat.data.LocationService
import io.github.kabirnayeem99.alarmforsalat.data.LocationService.LocationResult
import io.github.kabirnayeem99.alarmforsalat.databinding.ActivityAlarmForSalatBinding
import io.github.kabirnayeem99.alarmforsalat.repos.AdhanRepo
import io.github.kabirnayeem99.alarmforsalat.service.db.SalatTimingsDatabase
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.AlarmFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.MapsFragment
import io.github.kabirnayeem99.alarmforsalat.ui.fragments.SettingsFragment
import io.github.kabirnayeem99.alarmforsalat.ui.viewmodels.AdhanViewModel
import io.github.kabirnayeem99.alarmforsalat.ui.viewmodels.AdhanViewModelFactory
import io.github.kabirnayeem99.alarmforsalat.utils.SettingsManager


const val ACCESS_CODE_LOCATION = 1

class AlarmForSalatActivity : AppCompatActivity() {
    private lateinit var fragmentAlarm: AlarmFragment
    private lateinit var fragmentLocation: MapsFragment
    private lateinit var binding: ActivityAlarmForSalatBinding
    lateinit var viewModel: AdhanViewModel

    private val settingsManager = SettingsManager.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmForSalatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setUpViewModel()
        initFragments()
        initTabLayout()
        setUpPopUpMenu()
        requestLocationPermission()
        getUserLocation()
    }

    private fun setUpPopUpMenu() {
        binding.ivMoreHoriz.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu);
            popup.setOnMenuItemClickListener {
                Log.d(TAG, "setUpPopUpMenu: you clicked upon it")
                loadFragment(SettingsFragment())
                true
            }
            popup.show()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.flMain, fragment)
        transaction.commit()
    }


    private fun setUpViewModel() {

        val db = SalatTimingsDatabase(this)
        val repo = AdhanRepo(db)
        lateinit var viewModelFactory: AdhanViewModelFactory
        var cityName: String = "Dhaka"
        var countryName: String = "Bangladesh"


        viewModelFactory = AdhanViewModelFactory(repo, cityName, countryName)

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


    companion object {
        private const val TAG = "AlarmForSalatActivity"
    }

}