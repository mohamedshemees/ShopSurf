package dev.mo.surfcart.registration.data

import android.util.Log
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.providers.builtin.OTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val supaAuth: Auth
) : AuthRepository {
    override suspend fun sendOtp(email: String) {
        withContext(Dispatchers.IO) {
           val test=supaAuth.signInWith(OTP) {
                this.email = email
            }
            supaAuth

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

    override suspend fun resendOtp(email: String) {
        withContext(Dispatchers.IO) {
            supaAuth.signInWith(OTP) {
                this.email = email
                createUser = false
            }

        }
    }

    override suspend fun getCurrentUser(): String {
        return supaAuth.currentUserOrNull()?.id.toString()
    }
}