package dev.mo.surfcart.product_details.usecase

import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.core.entity.ProductDetails
import dev.mo.surfcart.core.repository.ProductRepository
import javax.inject.Inject

class GetProductDetails @Inject constructor(
    private val productRepository: ProductRepository

){
    suspend fun getProductDetails(productId: Long) : Map<String, String> {
       return productRepository.getProductDetails(productId)

    }
    suspend fun getProductInstanceDetails(productId: String):Product {
        return productRepository.getProductInstanceDetails(productId)
    }
}
