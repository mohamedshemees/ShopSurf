package dev.mo.surfcart.products.usecase

import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.core.repository.ProductRepository
import javax.inject.Inject

class GetProductsBySubCategoryUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend fun invoke(categoryId: Long): List<Product> {
        return  productRepository.getAllProductsOfCategory(categoryId)
}
    suspend fun getProductsOfSupCategory(categoryId: Long): List<Product> {
        return productRepository.getProductsByCategory(categoryId)
    }

}