package dev.mo.surfcart.account.domain.usecase

import dev.mo.surfcart.account.domain.repository.AccountRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor (
    private val userRepository: AccountRepository
) {
    suspend operator fun invoke() = userRepository.getUserData()
}
