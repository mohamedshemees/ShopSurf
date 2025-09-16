package dev.mo.surfcart.registration.usecase

import dev.mo.surfcart.registration.data.AuthRepository
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository

){
    suspend fun verifyOtp(email: String, otp: String):Boolean {
       return authRepository.verifyOtp(email,otp)
    }
}