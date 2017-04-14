package seeseost.de.simpleleak

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), LocationListener {

    private val REQ_CODE_LOCATION_PERM = 0

    private val TAG: String = "MainActivity"

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        checkLocationServicePermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location?) {
        Log.d(TAG, "onLocationChanged " + location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(TAG, "onStatusChanged")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(TAG, "onProviderEnabled " + provider)
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(TAG, "onProviderDisabled " + provider)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionsResult")
        when (requestCode) {
            REQ_CODE_LOCATION_PERM -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocationManager()
                }
            }
        }
    }

    fun checkLocationServicePermission() {
        Log.d(TAG, "checkLocationServicePermission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var coarse = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            var fine = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (coarse && fine) {
                initLocationManager()
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest
                        .permission.ACCESS_FINE_LOCATION), REQ_CODE_LOCATION_PERM)
            }
        } else {
            initLocationManager()
        }
    }

    fun initLocationManager() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeUnit
                .MINUTES.toMillis(5), 100f, this)
    }
}
