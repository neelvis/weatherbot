package com.neelvis.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.neelvis.R
import com.neelvis.model.data.LocationService


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

        try {
//            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            LocationServices
                .getFusedLocationProviderClient(LocationService.appContext)
                .lastLocation.addOnSuccessListener {
                    LocationService.currentLocation = it
                }
        } catch (e: SecurityException) {
            //TODO: Handle exception
        }

    }

    override fun onStart() {
        super.onStart()

        checkLocationPermission()

        if (LocationService.locationManager == null) {
            LocationService.locationManager =
                ContextCompat.getSystemService(this, LocationManager::class.java)!!
        }
        val isGpsEnabled = LocationService.locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            startActivity(
                Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS
                )
            )
            //TODO: show call to action to enable gps
        }


    }
}
