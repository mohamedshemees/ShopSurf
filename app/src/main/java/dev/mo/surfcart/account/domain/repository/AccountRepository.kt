package dev.mo.surfcart.account.domain.repository

import dev.mo.surfcart.account.data.dto.CustomerAddress
import dev.mo.surfcart.checkout.ui.PaymentMethodItem
import java.util.UUID

interface AccountRepository {
    suspend fun getCustomerAddresses(): List<CustomerAddress>
    suspend fun getPaymentMethods(): List<PaymentMethodItem>
    suspend fun placeOrder(
        addressId: UUID,
        paymentMethodId: UUID
    )
}