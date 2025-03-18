package dev.mo.surfcart.core.repository

import android.util.Log
import dev.mo.surfcart.core.dto.CategoryDto
import dev.mo.surfcart.core.dto.ParentCategoryMapper.toCategory
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.core.dto.ProductMapper.toProduct
import dev.mo.surfcart.core.dto.ProductDto
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.banner
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
)
    : ProductRepository {
    override suspend fun getTopLevelCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            postgrest.from("category")
                .select()
                .decodeList<CategoryDto>()
                .filter { it.parentId == null }
                .map { it.toCategory() }
        }

    }

    override suspend fun getFeaturedProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            postgrest.from("product")
                .select()
                .decodeList<ProductDto>()
                .map { it.toProduct() }
        }
    }

    override suspend fun getSubCategories(parentId: Long): List<Category> {
        return withContext(Dispatchers.IO) {
            postgrest.from("category")
                .select()
                .decodeList<CategoryDto>()
                .filter { it.parentId == parentId }
                .map { it.toCategory() }
        }
    }

    override suspend fun getProductsByCategory(categoryId: Long): List<Product> {
        Log.d("wow","getProductsByCategory repo  ")
        return withContext(Dispatchers.IO) {
            postgrest.from("product")
                .select()
                .decodeList<ProductDto>()
                .filter { it.categoryId == categoryId }
                .map { it.toProduct() }

        }
    }

    override suspend fun getAllProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            postgrest.from("product")
                .select()
                .decodeList<ProductDto>()
                .map { it.toProduct() }
        }
    }

    override suspend fun getBanners(): List<String> {
        return  postgrest.from("banner")
            .select()
            .decodeList<banner>()
            .map { it.url }
    }

    override suspend fun getOnSaleroducts(): List<Product> {
        return withContext(Dispatchers.IO){
            postgrest.from("product")
                .select()
                .decodeList<ProductDto>()
                .filter { it.discountPrice != null && it.discountPrice > 0 }
                .map { it.toProduct() }
        }
    }
}

