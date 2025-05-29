package dev.mo.surfcart.core.repository

import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product

interface ProductRepository {
    suspend fun getTopLevelCategories(): List<Category>
    suspend fun getFeaturedProducts(): List<Product>
    suspend fun getSubCategories(parentId: Long): List<Category>
    suspend fun getProductsByCategory(categoryId: Long): List<Product>
    suspend fun getAllProductsOfCategory(categoryId: Long): List<Product>
    suspend fun getBanners(): List<String>
    suspend fun getOnSaleProducts(): List<Product>
    suspend fun getProductDetails(productId: Long): Map<String, String>
    suspend fun getProductInstanceDetails(productId: String): Product
    suspend fun getProductById(productId: Long): Product

}