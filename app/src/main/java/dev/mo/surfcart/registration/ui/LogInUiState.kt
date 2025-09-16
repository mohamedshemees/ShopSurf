package dev.mo.surfcart.registration.ui

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val isFormValid: Boolean = false
)

sealed interface LogInUiState {
    data class FormInput(val data: LoginFormState = LoginFormState()) : LogInUiState
    object Loading : LogInUiState
    data class Success(val message: String) : LogInUiState
    data class Error(val message: String) : LogInUiState
}