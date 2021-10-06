package com.neelvis.model.data

import android.app.*
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import androidx.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.neelvis.model.repository.API_KEY


data class RequestData(val latitude: String = "0.0", val longitude: String = "0.0", val apiKey: String = API_KEY)

class LocationUpdateFgService : Service() {

    companion object {
        private const val REQUEST_CODE = 0
        private const val REQUEST_LOCATION_KEY = "requesting_location_updates"
        private val TAG = LocationUpdateFgService.javaClass.simpleName
        private const val PACKAGE_NAME = "com.neelvis.model.data"
        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"
        private const val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"
        private const val CHANNEL_ID = "channel_01"
        private const val UPDATE_INTERVAL = 600_000L
        private const val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2
        private const val NOTIFICATION_ID = 11111111
        //lateinit var appContext: Context
        private var isRunning = false
        private var currentLocation: Location? = null

        fun getLocationRequestData(): RequestData {
            return RequestData(
                currentLocation?.latitude?.toString() ?: "0.0",
                currentLocation?.longitude?.toString() ?: "0.0",
                API_KEY
            )
        }
    }

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var isChangingConfiguration = false
    private lateinit var notificationManager: NotificationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var serviceHandler: Handler

    private val iBinder = LocalBinder()

    override fun onCreate() {
        isRunning = true
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }

        createLocationRequest()
        getLastLocation()

        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        serviceHandler = Handler(handlerThread.looper)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                "Location updater",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        super.onCreate()
    }

    override fun onDestroy() {
        isRunning = false
        serviceHandler.removeCallbacksAndMessages(null)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isChangingConfiguration = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(TAG, "MainFragment is Binding")
        stopForeground(true)
        isChangingConfiguration = false
        return iBinder
    }

    override fun onRebind(intent: Intent?) {
        Log.i(TAG, "MainFragment is ReBinding")
        stopForeground(true)
        isChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "MainFragment is UnBinding")
        if (!isChangingConfiguration) {
            Log.i(TAG, "starting foreground service")
            startForeground(NOTIFICATION_ID, getNotification())
        }
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Location service started")
        val startedFromNotification = intent?.getBooleanExtra(
            EXTRA_STARTED_FROM_NOTIFICATION,
            false
        )
        //TODO: implement EXTRA_STARTED_FROM_NOTIFICATION whether we need it


        return super.onStartCommand(intent, flags, startId)
    }

    fun requestLocationUpdates() {
        Log.i(TAG, "requesting location updates")
        setRequestingLocationUpdates(true)
        startService(Intent(this.applicationContext, LocationUpdateFgService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } catch (e: SecurityException) {
            setRequestingLocationUpdates(false)
            Log.e(TAG, "Location permission DENIED. Can't initiate location update request.\n$e")
            //TODO: Implement dialog to grant a permission
        }
    }

    fun refuseLocationUpdates() {
        Log.i(TAG, "refusing location updates")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        setRequestingLocationUpdates(false)
        stopSelf()
    }

    private fun setRequestingLocationUpdates(value: Boolean) {
        PreferenceManager
            .getDefaultSharedPreferences(this)
            .edit()
            .putBoolean(REQUEST_LOCATION_KEY, value)
            .apply()
    }

    private fun getNotification(): Notification {
        val intent = Intent(this.javaClass.simpleName)
        val text = "${currentLocation?.latitude ?: "Unknown"} ${currentLocation?.longitude ?: "location"}"
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        val servicePendingIntent: PendingIntent = PendingIntent
            .getService(
                this,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        return NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setWhen(System.currentTimeMillis())
            .build()
    }

    private fun getLastLocation() {
        try {
            fusedLocationProviderClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        currentLocation = task.result
                    } else {
                        Log.w(
                            TAG,
                            "Failed to get location."
                        )
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(
                TAG,
                "Lost location permission.$unlikely"
            )
        }
    }

    private fun onNewLocation(_location: Location) {
        Log.i(TAG, "Location: ${_location.latitude}, ${_location.longitude}")
        currentLocation = _location

        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(ACTION_BROADCAST)
                    .putExtra(EXTRA_LOCATION, _location)
            )
        if (isRunning) {
            notificationManager.notify(NOTIFICATION_ID, getNotification())
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest
            .create()
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
    }

    inner class LocalBinder : Binder() {
        fun getLocationService(): LocationUpdateFgService {
            return this@LocationUpdateFgService
        }
    }


//
//    var locationManager: LocationManager? = null
//    var appContext: Context? = null
//
//    private fun getLocation(): Location? {
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)
//        var location: Location? = null
//
//        return try {
////            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
//                location = it
//            }
//            location
//        } catch (e: SecurityException) {
//            //TODO: Handle exception
//            null
//        }
//    }
//

//
//    override fun onBind(intent: Intent?): IBinder? {
//        TODO("Not yet implemented")
//    }
//
//    fun startLocationUpdates() {
//        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)
//
//        val locationRequest = LocationRequest.create().setInterval(60_000)
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(p0: LocationResult) {
//                super.onLocationResult(p0)
//                currentLocation = p0.lastLocation
//            }
//
//        }
//        fusedLocationProviderClient.requestLocationUpdates(
//
//        )
//    }


}