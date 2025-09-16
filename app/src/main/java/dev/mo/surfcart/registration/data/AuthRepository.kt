package dev.mo.surfcart.registration.data

interface AuthRepository {
    suspend fun sendOtp(email: String)
    suspend fun verifyOtp( email: String, otp: String): Boolean
    suspend fun getCurrentUser(): String
    suspend fun register(
        email: String,
        password: String,
        name: String,
        phone: String,
        role: String
    ): Boolean

    suspend fun login(email: String, password: String): Boolean
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean

}
