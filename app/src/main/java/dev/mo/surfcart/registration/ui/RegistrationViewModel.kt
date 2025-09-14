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
    private val _otpVerified = MutableStateFlow(false)
    val otpVerified = _otpVerified

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Success())
    val uiState = _uiState

    private fun updateForm(transform: (CreateAccountUiState) -> CreateAccountUiState) {
        val current = _uiState.value
        if (current is RegistrationUiState.Success) {
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
    Log.d("WOW", "onCreateAccountClicked: ")
        viewModelScope.launch {
            val currentState = _uiState.value
            Log.d("WOW", "launc: ")

            if (currentState is RegistrationUiState.Success) {
                try {
                    Log.d("WOW", "try: ")

                    registrationUseCase(
                        name = currentState.data.name,
                        phone = currentState.data.phone,
                        email = currentState.data.email,
                        password = currentState.data.password,
                        role = currentState.data.userType
                    )
                } catch (e: Exception) {
                    Log.d("WOW", "Exception: ",e)

                }
            }
        }
    }

    init {
        viewModelScope.launch {
            //sendOtpUseCase.getCurrentUser()
        }
    }

    private var email: String? = null

    suspend fun sendOtp(email: String) {
        if (!isValidEmail(email)) {
            _uiState.value = RegistrationUiState.Error("Invalid email address")
            return
        }
        this.email = email
        try {
            //sendOtpUseCase.sendOtp(email)
            _uiState.value = RegistrationUiState.OtpSent
        } catch (e: Exception) {
            _uiState.value = RegistrationUiState.Error("Failed to send OTP: ${e.message}")
        }
    }

    suspend fun verifyOtp(otp: String) {
        email?.let { email ->
            try {
                //val success = sendOtpUseCase.verifyOtp(email, otp)
                //_uiState.value = if (success) {
                //RegistrationUiState.OtpVerified
                // } else {
                //    RegistrationUiState.Error("Invalid OTP")
                //  }
            } catch (e: Exception) {
                _uiState.value = RegistrationUiState.Error("Verification failed: ${e.message}")
            }
        } ?: run {
            _uiState.value = RegistrationUiState.Error("Email not set")
        }
    }

    suspend fun resendOtp() {
        email?.let { email ->
            try {
                // sendOtpUseCase.resendOtp(email)
                _uiState.value = RegistrationUiState.OtpResent
            } catch (e: Exception) {
                _uiState.value = RegistrationUiState.Error("Failed to resend OTP: ${e.message}")
            }
        } ?: run {
            _uiState.value = RegistrationUiState.Error("Email not set")
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

