package dev.mo.surfcart.registration.ui

sealed class RegistrationUiState {
    data class Success(val data: CreateAccountUiState= CreateAccountUiState()): RegistrationUiState()
    object OtpSent : RegistrationUiState()
    object OtpVerified : RegistrationUiState()
    object OtpResent : RegistrationUiState()
    data class Error(val message: String) : RegistrationUiState()
}
data class CreateAccountUiState(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val userType: String="customer"
)
fun CreateAccountUiState.toDomain(){

}