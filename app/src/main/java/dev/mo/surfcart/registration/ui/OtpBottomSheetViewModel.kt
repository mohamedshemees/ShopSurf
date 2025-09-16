package dev.mo.surfcart.registration.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.registration.usecase.VerifyOtpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
    class OtpBottomSheetViewModel @Inject constructor(
        val verifyOtpUseCase: VerifyOtpUseCase
    ) : ViewModel() {

        private val _uiState = MutableStateFlow<OtpSheetUiState>(OtpSheetUiState.Idle)
        val uiState: StateFlow<OtpSheetUiState> = _uiState
        fun verifyOtp(email:String,otp: String) {
            // Basic client-side validation (can be enhanced)
            if (otp.length != 6 || !otp.all { it.isDigit() }) {
                _uiState.value = OtpSheetUiState.Error("OTP must be 6 digits.")
                return
            }
            _uiState.value = OtpSheetUiState.Loading
            viewModelScope.launch {
                try {
                   val success = verifyOtpUseCase.verifyOtp( email,otp)
                    Log.d("WOW", "verifyOtp: $success")
                    if (success) {
                        _uiState.value = OtpSheetUiState.OtpVerified
                    } else {
                        _uiState.value = OtpSheetUiState.Error("Invalid OTP. Please try again.")
                    }
                } catch (e: Exception) {
                    Log.d("WOW", "Exception: ",e)
                    _uiState.value = OtpSheetUiState.Error(e.message ?: "OTP verification failed.")
                }
            }
        }


        fun clearError() {
            if (_uiState.value is OtpSheetUiState.Error) {
                _uiState.value = OtpSheetUiState.Idle
            }
        }
    }
sealed class OtpSheetUiState {
    data object Idle : OtpSheetUiState() // Initial state, ready for input
    data object Loading : OtpSheetUiState() // Verification in progress
    data object OtpVerified : OtpSheetUiState() // OTP successfully verified
    data class Error(val message: String) : OtpSheetUiState()
}