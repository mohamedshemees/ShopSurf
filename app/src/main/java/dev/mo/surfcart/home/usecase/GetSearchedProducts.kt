package dev.mo.surfcart.home.usecase

import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.core.repository.ProductRepository
import javax.inject.Inject

class GetSearchedProducts@Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend fun getOnSaleProducts(query: String): List<Product> {
        return productRepository.getSearchResult(query)
    }
}