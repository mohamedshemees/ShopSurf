package dev.mo.surfcart

import dev.mo.surfcart.registration.data.AuthRepository
import javax.inject.Inject

class CheckIfLoggedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
){
    suspend operator fun invoke(): Boolean =authRepository.isLoggedIn()
}