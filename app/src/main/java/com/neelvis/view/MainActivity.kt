package com.neelvis.view

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.neelvis.R
import com.neelvis.model.data.LocationUpdateFgService
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "MAIN ACTIVITY"
const val PERMISSION_REQUEST_LOCATION = 0

//TODO: move extensions to separate file, like they do it @ Google

fun AppCompatActivity.isPermissionDenied(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED

fun View.showSnackbar(messageID: Int, duration: Int) = Snackbar.make(this, messageID, duration).show()

fun View.showSnackbar(
    messageID: Int,
    duration: Int,
    actionMessageID: Int,
    action: (View) -> Unit
) = Snackbar.make(this, messageID, duration)
        .setAction(actionMessageID) {
            action(this)
        }
        .show()

fun IntArray.containsOnly(value: Int) = this.size == 1 && this[0] == value

class MainActivity : AppCompatActivity(),
    ActivityCompat.OnRequestPermissionsResultCallback{

    //TODO: Dialog when GPS is off

    private var locationService: LocationUpdateFgService? = null
    private var locationServiceIsBound = false

    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.activity_main)

        locationService = LocationUpdateFgService()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main, MainFragment.newInstance())
                .commitNow()
        }

//        LocationUpdateFgService.locationManager =
//            ContextCompat.getSystemService(this, LocationManager::class.java)!!
//
//        try {
////            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            LocationServices
//                .getFusedLocationProviderClient(LocationUpdateFgService.appContext)
//                .lastLocation.addOnSuccessListener {
//                    LocationUpdateFgService.currentLocation = it
//                }
//        } catch (e: SecurityException) {
//            //TODO: Handle exception
//        }

    }

    override fun onStart() {
        super.onStart()

        checkLocationPermission()

//        if (LocationUpdateFgService.locationManager == null) {
//            LocationUpdateFgService.locationManager =
//                ContextCompat.getSystemService(this, LocationManager::class.java)!!
//        }
//        val isGpsEnabled = LocationUpdateFgService.locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        if (!isGpsEnabled) {
//            startActivity(
//                Intent(
//                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
//                )
//            )
//            //TODO: show call to action to enable gps
//        }
    }


    private fun checkLocationPermission()
    {
        if (isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION) ||
            isPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                layout.showSnackbar(
                    R.string.location_permission_required,
                    Snackbar.LENGTH_INDEFINITE,
                    R.string.OK
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        PERMISSION_REQUEST_LOCATION
                    )
                }
            } else {
                layout.showSnackbar(R.string.location_permission_denied, Snackbar.LENGTH_SHORT)
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_REQUEST_LOCATION
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            Log.i(TAG, "Processing location permission request result")
            if (grantResults.containsOnly(PackageManager.PERMISSION_GRANTED)) {
                Log.i(TAG, "Location permission granted")
            } else {
                Log.i(TAG, "Location permission DENIED.")
                layout.showSnackbar(
                    R.string.location_permission_denied,
                    Snackbar.LENGTH_INDEFINITE
                )
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            locationService = (service as LocationUpdateFgService.LocalBinder).getLocationService()
            locationServiceIsBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            locationService = null
            locationServiceIsBound = false
        }
    }
}

