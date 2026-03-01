package com.lds.cuidar.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.cuidar.domain.location.LocationService
import com.lds.cuidar.domain.model.PanicEvent
import com.lds.cuidar.domain.usecase.TriggerPanicUseCase
import kotlinx.coroutines.launch

class PanicViewModel(
    private val triggerPanicUseCase: TriggerPanicUseCase,
    private val locationService: LocationService
) : ViewModel() {

    fun onPanicClicked() {
        viewModelScope.launch {
            Log.d("PANIC_FLOW", "Button clicked")

            val location = locationService.getCurrentLocation()
            Log.d("PANIC_FLOW", "Location obtained: $location")

            location?.let {
                Log.d("PANIC_FLOW", "Calling repository")

                val result = triggerPanicUseCase(
                    PanicEvent(
                        userId = "test-user",
                        latitude = it.first,
                        longitude = it.second,
                        timestamp = System.currentTimeMillis()
                    )
                )

                Log.d("PANIC", "Sent to backend: $result")
            }
        }
    }
}
