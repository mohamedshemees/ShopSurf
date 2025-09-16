package dev.mo.surfcart.account.usecase

import dev.mo.surfcart.registration.data.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
   private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.logout()
}