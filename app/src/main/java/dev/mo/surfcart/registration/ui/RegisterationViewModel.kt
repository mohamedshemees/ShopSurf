package dev.mo.surfcart.registration.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.registration.usecase.SendOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterationViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase

) : ViewModel() {
    private val _otpVerified = MutableStateFlow(false)
    val otpVerified = _otpVerified

    private val _uiState= MutableStateFlow<RegistrationUiState>(RegistrationUiState.Default)
    val uiState = _uiState



    private var email: String? = null

    suspend fun sendOtp(email: String) {
        if (!isValidEmail(email)) {
            _uiState.value = RegistrationUiState.Error("Invalid email address")
            return
        }
        this.email = email
        try {
            sendOtpUseCase.sendOtp(email) // Suspend call
            _uiState.value = RegistrationUiState.OtpSent
        } catch (e: Exception) {
            _uiState.value = RegistrationUiState.Error("Failed to send OTP: ${e.message}")
        }
    }

    suspend fun verifyOtp(otp: String) {
        email?.let { email ->
            try {
                val success = sendOtpUseCase.verifyOtp(email, otp) // Suspend call
                _uiState.value = if (success) {
                    RegistrationUiState.OtpVerified
                } else {
                    RegistrationUiState.Error("Invalid OTP")
                }
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
                sendOtpUseCase.resendOtp(email) // Suspend call
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

