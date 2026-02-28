package com.lds.cuidar.ui.theme
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.cuidar.domain.LocationService
import kotlinx.coroutines.launch


class PanicViewModel(
    private val locationService: LocationService
) : ViewModel() {

    fun onPanicClicked() {
        viewModelScope.launch {
            val location = locationService.getCurrentLocation()

            location?.let {
                Log.d("PANIC", "Lat: ${it.first}, Lng: ${it.second}")
            } ?: Log.d("PANIC", "Location not available")
        }
    }
}
