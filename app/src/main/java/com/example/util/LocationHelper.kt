package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

/**
 * LocationHelper manages user location retrieval and geographic computations
 * for farm-to-vet mapping, veterinary dispatch, and distance calculation.
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    companion object {
        // Default rural farm center coordinates (e.g., Jaipur rural cluster)
        const val DEFAULT_FARM_LAT = 26.9180
        const val DEFAULT_FARM_LON = 75.7950

        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        /**
         * Checks if the app currently holds location permissions.
         */
        fun hasLocationPermission(context: Context): Boolean {
            val fine = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val coarse = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            return fine || coarse
        }

        /**
         * Calculates distance between two coordinates in kilometers.
         */
        fun calculateDistanceKm(
            startLat: Double,
            startLon: Double,
            endLat: Double,
            endLon: Double
        ): Double {
            val results = FloatArray(1)
            Location.distanceBetween(startLat, startLon, endLat, endLon, results)
            return (results[0] / 1000.0)
        }

        /**
         * Formats distance cleanly for UI display (e.g., "850 m" or "2.4 km").
         */
        fun formatDistance(distanceKm: Double): String {
            return if (distanceKm < 1.0) {
                val meters = (distanceKm * 1000).toInt()
                "$meters m"
            } else {
                String.format(Locale.getDefault(), "%.1f km", distanceKm)
            }
        }

        /**
         * Builds an Android navigation Intent targeting Google Maps or any installed map client.
         */
        fun getDirectionsIntent(
            destLat: Double,
            destLon: Double,
            label: String? = null
        ): Intent {
            val uriString = if (!label.isNullOrBlank()) {
                "geo:$destLat,$destLon?q=$destLat,$destLon(${Uri.encode(label)})"
            } else {
                "google.navigation:q=$destLat,$destLon"
            }
            return Intent(Intent.ACTION_VIEW, Uri.parse(uriString)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }

        /**
         * Creates an Intent to launch turn-by-turn directions from the farm to the veterinary clinic.
         */
        fun getTurnByTurnDirectionsIntent(
            destLat: Double,
            destLon: Double
        ): Intent {
            val uri = Uri.parse("google.navigation:q=$destLat,$destLon&mode=d")
            return Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    /**
     * Checks if location permission is granted for this instance.
     */
    fun hasPermission(): Boolean = hasLocationPermission(context)

    /**
     * Asynchronously retrieves the last known location.
     * Returns null if permission is denied or location is not yet cached.
     */
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Location? {
        if (!hasPermission()) return null

        return suspendCancellableCoroutine { continuation ->
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener {
                        continuation.resume(null)
                    }
                    .addOnCanceledListener {
                        continuation.resume(null)
                    }
            } catch (e: SecurityException) {
                continuation.resume(null)
            }
        }
    }

    /**
     * Requests a fresh current location fix using the specified priority.
     * Defaults to Priority.PRIORITY_HIGH_ACCURACY.
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY
    ): Location? {
        if (!hasPermission()) return null

        return suspendCancellableCoroutine { continuation ->
            try {
                val request = CurrentLocationRequest.Builder()
                    .setPriority(priority)
                    .setMaxUpdateAgeMillis(30_000)
                    .setDurationMillis(10_000)
                    .build()

                fusedLocationClient.getCurrentLocation(request, null)
                    .addOnSuccessListener { location ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener {
                        continuation.resume(null)
                    }
                    .addOnCanceledListener {
                        continuation.resume(null)
                    }
            } catch (e: SecurityException) {
                continuation.resume(null)
            }
        }
    }

    /**
     * Provides a continuous cold Flow of location updates.
     * Updates automatically stop when the Flow collector is cancelled.
     */
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(
        intervalMs: Long = 10_000L,
        minUpdateIntervalMs: Long = 5_000L,
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY
    ): Flow<Location> = callbackFlow {
        if (!hasPermission()) {
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(priority, intervalMs)
            .setMinUpdateIntervalMillis(minUpdateIntervalMs)
            .setWaitForAccurateLocation(false)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    /**
     * Finds the nearest veterinary location from a list given the user's current farm coordinates.
     */
    fun <T> findNearestDestination(
        currentLat: Double,
        currentLon: Double,
        destinations: List<T>,
        latSelector: (T) -> Double,
        lonSelector: (T) -> Double
    ): Pair<T, Double>? {
        if (destinations.isEmpty()) return null

        return destinations
            .map { dest ->
                val dist = calculateDistanceKm(
                    currentLat,
                    currentLon,
                    latSelector(dest),
                    lonSelector(dest)
                )
                Pair(dest, dist)
            }
            .minByOrNull { it.second }
    }
}
