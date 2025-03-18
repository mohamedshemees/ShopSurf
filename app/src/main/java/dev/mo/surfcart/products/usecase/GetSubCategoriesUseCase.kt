package dev.mo.surfcart.products.usecase

import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.repository.ProductRepository
import javax.inject.Inject

class GetSubCategoriesUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend fun invoke(parentId: Long): List<Category> {
        return productRepository.getSubCategories(parentId)
}
}
