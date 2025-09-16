package dev.mo.surfcart.registration.data

import android.util.Log
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.OTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val supaAuth: Auth
) : AuthRepository {
    override suspend fun sendOtp(email: String) {
        withContext(Dispatchers.IO) {
            supaAuth.signInWith(OTP) {
                this.email = email
            }
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supaAuth.verifyEmailOtp(
                    email = email, token = otp, type = OtpType.Email.EMAIL
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }


    override suspend fun getCurrentUser(): String {
        return supaAuth.currentUserOrNull()?.id.toString()
    }

    override suspend fun register(
        email: String, password: String, name: String, phone: String, role: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supaAuth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = buildJsonObject {
                        put("phone_number", phone)
                        put("full_name", name)
                        put("user_type", role)
                    }
                }

                true
            } catch (e: Exception) {
                Log.d("catch", "register: ${e.message}", e)
                false
            }
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                supaAuth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun logout(){
        supaAuth.signOut()
    }

    override suspend fun isLoggedIn(): Boolean {
        supaAuth.loadFromStorage()
       return withContext(Dispatchers.IO) { supaAuth.currentUserOrNull() != null }
}}