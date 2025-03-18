package dev.mo.surfcart.home.usecase

import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.repository.ProductRepository
import javax.inject.Inject

class GetParentCategoriesUseCase @Inject constructor(
    private val productRepository: ProductRepository
){

    suspend fun invoke(): List<Category> {
      return  productRepository.getTopLevelCategories()
    }

    suspend fun getBanners():List<String> {
        return productRepository.getBanners()
    }
}