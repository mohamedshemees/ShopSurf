package dev.mo.surfcart.registration.ui

sealed class RegistrationUiState {
    object Default : RegistrationUiState()
    object OtpSent : RegistrationUiState()
    object OtpVerified : RegistrationUiState()
    object OtpResent : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}