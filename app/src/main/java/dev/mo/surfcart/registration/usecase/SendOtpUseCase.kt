package dev.mo.surfcart.registration.usecase

import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository

){
    suspend  fun sendOtp(email: String) {
        authRepository.sendOtp(email)
    }

    suspend fun verifyOtp(email: String, otp: String):Boolean {
       return authRepository.verifyOtp(email, otp)

    }
    suspend fun resendOtp(email: String) {
        authRepository.resendOtp(email)
    }


}