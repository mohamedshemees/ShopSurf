package dev.mo.surfcart.registration.data

interface AuthRepository {

    suspend fun sendOtp(email: String)

    suspend fun verifyOtp(email: String, otp: String):Boolean

    suspend fun resendOtp(email: String)

    suspend fun getCurrentUser(): String

}
