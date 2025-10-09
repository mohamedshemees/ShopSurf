package dev.mo.surfcart.checkout.usecase

import dev.mo.surfcart.account.domain.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(addressId:UUID,paymentMethodId: UUID) {
        return accountRepository.placeOrder(
            addressId = addressId,
            paymentMethodId = paymentMethodId
        )
    }
}
