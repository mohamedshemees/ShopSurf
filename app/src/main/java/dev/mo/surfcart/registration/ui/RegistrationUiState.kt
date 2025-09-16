package dev.mo.surfcart.registration.ui

sealed class RegistrationUiState {
    data class TakingInput(val data: CreateAccountUiState=CreateAccountUiState()) : RegistrationUiState()
    object Loading : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
    data class OtpSent(val email: String) : RegistrationUiState()

}
data class CreateAccountUiState(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val userType: String="customer"
)
