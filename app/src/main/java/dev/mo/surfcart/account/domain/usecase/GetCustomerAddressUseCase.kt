package dev.mo.surfcart.account.domain.usecase

import dev.mo.surfcart.account.domain.repository.AccountRepository
import javax.inject.Inject

class GetCustomerAddressUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke()= accountRepository.getCustomerAddresses()
}