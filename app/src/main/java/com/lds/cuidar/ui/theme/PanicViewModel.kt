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

            val location = locationService.getCurrentLocation()

            location?.let {
                val result = repository.triggerPanic(
                    it.first,
                    it.second
                )

                Log.d("PANIC", "Sent to backend: $result")
            }
        }
    }
}
