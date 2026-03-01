package com.lds.cuidar.presentation.state

sealed class PanicUiState {
    object Idle : PanicUiState()
    object Sending : PanicUiState()
    object Sent : PanicUiState()
    data class Error(val message: String) : PanicUiState()
}
