package dev.mo.surfcart.checkout.usecase

import dev.mo.surfcart.cart.CartItem
import dev.mo.surfcart.cart.CartRepository
import javax.inject.Inject

class GetCheckoutProductsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): List<CartItem> {
        return cartRepository.getCartItems()
    }
}
