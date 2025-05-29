package dev.mo.surfcart.cart.usecase

import dev.mo.surfcart.cart.CartRepository
import javax.inject.Inject

class IncreaseQuantityUseCase @Inject constructor (
    private val cartRepository: CartRepository,
) {
    suspend operator fun invoke (productId: Int) {
        cartRepository.increaseQuantity(productId)
    }
}
