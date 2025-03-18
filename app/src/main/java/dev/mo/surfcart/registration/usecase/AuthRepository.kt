package dev.mo.surfcart.registration.usecase

interface AuthRepository {

    suspend fun sendOtp(email: String)

    suspend fun verifyOtp(email: String, otp: String):Boolean

    suspend fun resendOtp(email: String)

}
