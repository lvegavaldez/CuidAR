package com.lds.cuidar.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.lds.cuidar.domain.location.LocationService
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationServiceImpl(
    private val context: Context
) : LocationService {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocation(): Pair<Double, Double>? =
        suspendCancellableCoroutine { continuation ->

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(
                            Pair(location.latitude, location.longitude)
                        )
                    } else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
}
