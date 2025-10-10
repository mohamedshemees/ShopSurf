package dev.mo.surfcart.checkout.ui

import dev.mo.surfcart.account.data.dto.CustomerAddress
import dev.mo.surfcart.cart.CartItem
import java.util.UUID

data class CheckOutUiState(
    val userAddresses: List<CustomerAddress> = emptyList(),
    val checkoutProducts: List<CartItem> = emptyList(),
    val paymentMethods: List<PaymentMethodItem> = emptyList(),
    val isLoading: Boolean = true,
    val orderPlacedSuccessfully:Boolean=false,
    val errorMessage: String? = null,
    val selectedPaymentMethod: UUID? = null,
    val selectedUserAddresses: UUID? = null

)
