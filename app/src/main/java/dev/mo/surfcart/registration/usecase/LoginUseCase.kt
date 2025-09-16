package dev.mo.surfcart.registration.usecase

import dev.mo.surfcart.registration.data.AuthRepository
import javax.inject.Inject
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return authRepository.login(email, password)
    }
}