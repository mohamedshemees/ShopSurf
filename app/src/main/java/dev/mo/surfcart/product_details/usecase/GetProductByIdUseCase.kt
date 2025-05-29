package dev.mo.surfcart.product_details.usecase

import dev.mo.surfcart.core.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend fun getProductById(productId: Long) = productRepository.getProductById(productId)
}
