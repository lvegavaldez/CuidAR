package com.lds.cuidar.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.cuidar.domain.location.LocationService
import com.lds.cuidar.domain.model.PanicEvent
import com.lds.cuidar.domain.usecase.TriggerPanicUseCase
import com.lds.cuidar.presentation.navigation.NavigationEvent
import com.lds.cuidar.presentation.state.PanicUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PanicViewModel(
    private val triggerPanicUseCase: TriggerPanicUseCase,
    private val locationService: LocationService
) : ViewModel() {
    private val _uiState = MutableStateFlow<PanicUiState>(PanicUiState.Idle)
    val uiState: StateFlow<PanicUiState> = _uiState
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onPanicClicked() {
        if (_uiState.value is PanicUiState.Sending || _uiState.value is PanicUiState.Sent) {
            return
        }

        viewModelScope.launch {
            _uiState.value = PanicUiState.Sending

            val location = locationService.getCurrentLocation()

            if (location == null) {
                _uiState.value = PanicUiState.Error("Location unavailable")
                return@launch
            }

            val result = triggerPanicUseCase(
                PanicEvent(
                    userId = "test-user",
                    latitude = location.first,
                    longitude = location.second,
                    timestamp = System.currentTimeMillis()
                )
            )

            if (result.isSuccess) {
                _uiState.value = PanicUiState.Sent
                _navigationEvent.emit(NavigationEvent.OpenSafeWebsite)
            } else {
                _uiState.value = PanicUiState.Error("Network error")
            }
        }
    }
}
