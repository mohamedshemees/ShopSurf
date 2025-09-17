package dev.mo.surfcart.core.repository

import dev.mo.surfcart.core.dto.CategoryDto
import dev.mo.surfcart.core.dto.ParentCategoryMapper.toCategory
import dev.mo.surfcart.core.dto.ProductDto
import dev.mo.surfcart.core.dto.ProductMapper.toProduct
import dev.mo.surfcart.core.entity.Banner
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.core.entity.ProductDetails
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

    override suspend fun getAllProductsOfCategory(categoryId: Long): List<Product> {
        return withContext(Dispatchers.IO) {
            postgrest.rpc(
                "get_products_by_parent_category",
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
            .decodeList<Banner>()
            .map { it.url }
    }

    override suspend fun getOnSaleProducts(): List<Product> {
        return postgrest.rpc(
            "get_on_sale_products",
        )
            .decodeList<ProductDto>()
            .map { it.toProduct() }
    }

    override suspend fun getProductDetails(productId: Long): Map<String, String> {
        val details = withContext(Dispatchers.IO) {
            postgrest.rpc(
                "get_product_details_structured",
                JsonObject(
                    mapOf("p_product_id" to JsonPrimitive(productId))
                )
            ).decodeAs<ProductDetails>()
        }

        return details.properties
    }

    override suspend fun getProductInstanceDetails(productId: String): Product {
        return withContext(Dispatchers.IO) {
            postgrest.from("product_instance")
                .select()
                .decodeAs<ProductDto>()
                .toProduct()
        }
    }

    override suspend fun getProductById(productId: Long): Product {
        return withContext(Dispatchers.IO) {
            postgrest.from("product")
                .select()
                .decodeList<ProductDto>()
                .filter { it.productId == productId }
                .map { it.toProduct() }
                .first()
        }
    }
}

