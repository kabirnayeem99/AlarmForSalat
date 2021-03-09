package io.github.kabirnayeem99.alarmforsalat.data

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import java.util.*


class LocationService {
    /*

    source: https://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-on-a

    1. First of all I check what providers are enabled. Some may
        be disabled on the device, some may be disabled in application manifest.
    2. If any provider is available I start location listeners
        and timeout timer. It's half an hour.
    3. If I get update from location listener I use the provided
        value. I stop listeners and timer.
    4. If I don't get any updates and timer elapses I have to use
        last known values.
    5. I grab last known values from available providers and choose
        the most recent of them.
     */

    companion object {
        private const val TAG = "LocationService"
    }


    lateinit var firstTimer: Timer
    lateinit var locationManager: LocationManager
    lateinit var locationResult: LocationResult
    var isGpsEnabled = false
    var isNetworkEnabled = false

    fun getLocation(context: Context, result: LocationResult): Boolean {
        /*
            uses LocationResult callback class to pass location value from
            LocationService to user code.
         */
        locationResult = result

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //exceptions will be thrown if provider is not permitted.
        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            Log.e(TAG, "getLocation: $e")
        }
        try {
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            Log.e(TAG, "getLocation: $e")
        }

        //don't start listeners if no provider is enabled
        if (!isGpsEnabled && !isNetworkEnabled) return false

        // if gps provider enabled location manager will request update
        if (isGpsEnabled) locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            locationListenerGps
        )

        // if network provider enabled location manager will request update
        if (isNetworkEnabled) locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0f,
            locationListenerNetwork
        )

        firstTimer = Timer()
//        firstTimer.schedule(GetLastLocation(), 1800000) // half an hour
        firstTimer.schedule(GetLastLocation(), 20000) // 20 seconds

        return true
    }

    var locationListenerGps: LocationListener = object : LocationListener {

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onLocationChanged(location: Location) {
            firstTimer.cancel()
            locationResult.gotLocation(location)
            locationManager.removeUpdates(this)
            locationManager.removeUpdates(locationListenerNetwork)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    var locationListenerNetwork: LocationListener = object : LocationListener {

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onLocationChanged(location: Location) {
            firstTimer.cancel()
            locationResult.gotLocation(location)
            locationManager.removeUpdates(this)
            locationManager.removeUpdates(locationListenerGps)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    inner class GetLastLocation : TimerTask() {
        /*
            TimerTask class defines a task that can be
            scheduled to run for just once or for repeated number of time.
            This will be run after half an hour
         */

        override fun run() {
            locationManager.removeUpdates(locationListenerGps)
            locationManager.removeUpdates(locationListenerNetwork)
            var networkLocation: Location? = null
            var gpsLocation: Location? = null
            if (isGpsEnabled) gpsLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (isNetworkEnabled) networkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            //if there are both values use the latest one
            if (gpsLocation != null && networkLocation != null) {
                if (gpsLocation.time > networkLocation.time) locationResult.gotLocation(gpsLocation) else locationResult.gotLocation(
                    networkLocation
                )
                return
            }
            if (gpsLocation != null) {
                locationResult.gotLocation(gpsLocation)
                return
            }
            if (networkLocation != null) {
                locationResult.gotLocation(networkLocation)
                return
            }
            locationResult.gotLocation(null)
        }
    }

    abstract class LocationResult {
        abstract fun gotLocation(location: Location?)
    }
}