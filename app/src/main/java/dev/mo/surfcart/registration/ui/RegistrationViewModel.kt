package dev.mo.surfcart.registration.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.registration.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegisterUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.TakingInput())
    val uiState = _uiState.asStateFlow()

    private fun updateForm(transform: (CreateAccountUiState) -> CreateAccountUiState) {
        val current = _uiState.value
        if (current is RegistrationUiState.TakingInput) {
            _uiState.value = current.copy(data = transform(current.data))
        }
    }

    fun onNameChanged(value: String) = updateForm { it.copy(name = value) }
    fun onPhoneChanged(value: String) = updateForm { it.copy(phone = value) }
    fun onEmailChanged(value: String) = updateForm { it.copy(email = value) }
    fun onPasswordChanged(value: String) = updateForm { it.copy(password = value) }
    fun onConfirmPasswordChanged(value: String) = updateForm { it.copy(confirmPassword = value) }
    fun setUserType(userType: String) = updateForm { it.copy(userType = userType) }

    fun onCreateAccountClicked() {
        val currentState = _uiState.value
        if (currentState is RegistrationUiState.TakingInput) {
            _uiState.value = RegistrationUiState.Loading

            tryToExecute(
                call = {
                    registrationUseCase(
                        name = currentState.data.name,
                        phone = currentState.data.phone,
                        email = currentState.data.email,
                        password = currentState.data.password,
                        role = currentState.data.userType
                    )
                },
                onSuccess = {
                    _uiState.value = RegistrationUiState.OtpSent(email = currentState.data.email)
                },
                onError = { exception ->
                    _uiState.value = RegistrationUiState.Error(exception.message ?: "An unknown error occurred")
                }
            )
        }
    }

    fun resetToForm() {
        val currentData = (_uiState.value as? RegistrationUiState.TakingInput)?.data ?: CreateAccountUiState()
        _uiState.value = RegistrationUiState.TakingInput(currentData)
    }
}
