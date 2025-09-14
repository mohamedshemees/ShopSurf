package dev.mo.surfcart.registration.usecase

import dev.mo.surfcart.registration.data.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        phone: String,
        role:String
    ): Boolean {
        return authRepository.register(email, password,name, phone, role)
    }
}