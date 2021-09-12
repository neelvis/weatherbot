package com.neelvis.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.neelvis.R
import com.neelvis.model.LocationService
import com.neelvis.model.Model


class MainActivity : AppCompatActivity() {

    //TODO: Dialog when GPS is off

    private fun checkLocationPermission()
    {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        LocationService.locationManager =
            ContextCompat.getSystemService(this, LocationManager::class.java)!!
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermission()
        if (LocationService.locationManager == null) {
            LocationService.locationManager =
                ContextCompat.getSystemService(this, LocationManager::class.java)!!
        }
    }
}

//fun onStart() {
//    super.onStart()
//
//    // This verification should be done during onStart() because the system calls
//    // this method when the user returns to the activity, which ensures the desired
//    // location provider is enabled each time the activity resumes from the stopped state.
//    val locationManager = getSystemService<Any>(Context.LOCATION_SERVICE) as LocationManager?
//    val gpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
//    if (!gpsEnabled) {
//        // Build an alert dialog here that requests that the user enable
//        // the location services, then when the user clicks the "OK" button,
//        // call enableLocationSettings()
//    }
//}
//
//private fun enableLocationSettings() {
//    val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//    startActivity(settingsIntent)
//}