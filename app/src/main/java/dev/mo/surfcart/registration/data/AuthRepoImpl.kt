package dev.mo.surfcart.registration.data

import dev.mo.surfcart.core.safeSupabaseCall
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.OTP
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val supaAuth: Auth
) : AuthRepository {
    override suspend fun sendOtp(email: String) {
        safeSupabaseCall {
            supaAuth.signInWith(OTP) {
                this.email = email
            }
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Boolean {
        return safeSupabaseCall {
            supaAuth.verifyEmailOtp(
                email = email, token = otp, type = OtpType.Email.EMAIL
            )
            true
        }
    }


    override suspend fun getCurrentUser(): String {
        return safeSupabaseCall { supaAuth.currentUserOrNull()?.id.toString() }
    }

    override suspend fun register(
        email: String, password: String, name: String, phone: String, role: String
    ): Boolean {
        return safeSupabaseCall {
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
        }
    }

    override suspend fun login(email: String, password: String): Boolean {
        return safeSupabaseCall {
            supaAuth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            true
        }
    }

    override suspend fun logout() {
        safeSupabaseCall {
            supaAuth.signOut()
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return safeSupabaseCall {
            supaAuth.loadFromStorage()
            supaAuth.currentUserOrNull() != null
        }
    }
}
