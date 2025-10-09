package dev.mo.surfcart.core

sealed class UiEvent {
    data class ShowSuccessSnackbar(val message: String) : UiEvent()
    data class ShowErrorSnackbar(val message: String) : UiEvent()
}