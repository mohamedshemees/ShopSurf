package dev.mo.surfcart.checkout.usecase

import dev.mo.surfcart.account.domain.repository.AccountRepository
import dev.mo.surfcart.checkout.ui.PaymentMethodItem
import javax.inject.Inject

class GetPaymentMethodsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): List<PaymentMethodItem> {
        return accountRepository.getPaymentMethods()
    }
}
