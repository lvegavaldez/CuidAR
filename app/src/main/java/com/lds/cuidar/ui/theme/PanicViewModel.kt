package com.lds.cuidar.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.cuidar.data.repository.PanicRepository
import com.lds.cuidar.domain.LocationService
import kotlinx.coroutines.launch

class PanicViewModel(
    private val locationService: LocationService,
    private val repository: PanicRepository
) : ViewModel() {

    fun onPanicClicked() {
        viewModelScope.launch {
            Log.d("PANIC_FLOW", "Button clicked")

            val location = locationService.getCurrentLocation()
            Log.d("PANIC_FLOW", "Location obtained: $location")

            location?.let {
                Log.d("PANIC_FLOW", "Calling repository")
                val result = repository.triggerPanic(
                    it.first,
                    it.second
                )

                Log.d("PANIC", "Sent to backend: $result")
            }
        }
    }
}
