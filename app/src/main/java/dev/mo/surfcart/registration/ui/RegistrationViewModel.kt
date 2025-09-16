package dev.mo.surfcart.registration.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.registration.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegisterUseCase

) : ViewModel() {
    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.TakingInput())
    val uiState = _uiState

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
        viewModelScope.launch {

            val currentState = _uiState.value
            _uiState.value= RegistrationUiState.Loading
            if (currentState is RegistrationUiState.TakingInput) {
                try {
                    registrationUseCase(
                        name = currentState.data.name,
                        phone = currentState.data.phone,
                        email = currentState.data.email,
                        password = currentState.data.password,
                        role = currentState.data.userType
                    )
                   _uiState.value= RegistrationUiState.OtpSent(email = currentState.data.email)
                } catch (e: Exception) {
                    Log.d("WOW", "Exception: ",e)

                }
            }
        }
    }
}

