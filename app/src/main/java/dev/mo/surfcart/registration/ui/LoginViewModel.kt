package dev.mo.surfcart.registration.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.registration.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logInUserUseCase: LoginUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<LogInUiState>(LogInUiState.FormInput())
    val uiState = _uiState.asStateFlow()

    private fun updateForm(transform: (LoginFormState) -> LoginFormState) {
        _uiState.update { currentState ->
            if (currentState is LogInUiState.FormInput) {
                val updatedData = transform(currentState.data)
                currentState.copy(
                    data = updatedData.copy(
                        isFormValid = isInputValid(updatedData.email, updatedData.password)
                    )
                )
            } else {
                currentState
            }
        }
    }

    fun onEmailChanged(email: String) {
        updateForm { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        updateForm { it.copy(password = password) }
    }

    fun onLoginClicked() {
        val currentState = _uiState.value
        if (currentState is LogInUiState.FormInput) {
            val email = currentState.data.email
            val password = currentState.data.password
            if (!isInputValid(email, password)) {
                _uiState.value = LogInUiState.Error("Please enter valid email and password.")
                return
            }

            _uiState.value = LogInUiState.Loading
            tryToExecute(
                call = { logInUserUseCase(email, password) },
                onSuccess = { loginSuccessful ->
                    if (loginSuccessful) {
                        _uiState.value = LogInUiState.Success("Login Successful!")
                    } else {
                        _uiState.value = LogInUiState.Error("Invalid email or password.")
                    }
                },
                onError = { exception ->
                    _uiState.value = LogInUiState.Error("Login failed: ${exception.message}")
                }
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.isNotBlank() && password.length >= 6
    }

    private fun isInputValid(email: String, password: String): Boolean {
        return isValidEmail(email) && isValidPassword(password)
    }

    fun resetToForm() {
        val currentData = (_uiState.value as? LogInUiState.FormInput)?.data ?: LoginFormState()
        _uiState.value = LogInUiState.FormInput(currentData)
    }
}