package dev.mo.surfcart.core.repository

import android.util.Log
import dev.mo.surfcart.core.dto.CategoryDto
import dev.mo.surfcart.core.dto.ParentCategoryMapper.toCategory
import dev.mo.surfcart.core.dto.ProductDetailsDto
import dev.mo.surfcart.core.dto.ProductDto
import dev.mo.surfcart.core.dto.ProductMapper.toProduct
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.core.entity.ProductDetails
import dev.mo.surfcart.core.entity.banner
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : ProductRepository {
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

    override suspend fun getAllProductsofCategory(categoryId: Long): List<Product> {
        return withContext(Dispatchers.IO) {
            postgrest.rpc(
                "get_products_by_parent_category",  // SQL function name
                JsonObject(mapOf("parent_id" to JsonPrimitive(categoryId)))
            )
                .decodeList<ProductDto>()
                .map { it.toProduct() }
        }
    }


    override suspend fun getProductsByCategory(categoryId: Long): List<Product> {
        return withContext(Dispatchers.IO) {
            postgrest.rpc(
                "get_products_by_sup_category",
                JsonObject(mapOf("category_1id" to JsonPrimitive(categoryId)))
            )
                .decodeList<ProductDto>()
                .map { it.toProduct() }
        }

    }

    override suspend fun getBanners(): List<String> {
        return postgrest.from("banner")
            .select()
            .decodeList<banner>()
            .map { it.url }
    }

    override suspend fun getOnSaleroducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            postgrest.from("product")
                .select()
                .decodeList<ProductDto>()
                .filter { it.discountPrice != null && it.discountPrice > 0 }
                .map { it.toProduct() }
        }
    }

    override suspend fun getProductDetails(productId: Long): Map<String, String> {
        val details = withContext(Dispatchers.IO) {
            postgrest.rpc(
                "get_product_details_structured",
                JsonObject(mapOf("p_product_id" to JsonPrimitive(1L))
            )).decodeAs<ProductDetails>()
        }

        return details.properties
    }

    override suspend fun getProductInstanceDetails(productId: String): Product {
        return withContext(Dispatchers.IO){
            postgrest.from("product_instance")
                .select()
                .decodeAs<ProductDto>()
                .toProduct()
        }
    }


}

