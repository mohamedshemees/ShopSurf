package dev.mo.surfcart.core.entity

data class Product(
    val productId: Long,
    val timeCreated: String? = null,
    val sellerId: String,
    val productName: String,
    val productDescription: String,
    val modelName: String? = "Generic",
    val productPrice: Double?,
    val productCount: Long?,
    val productThumbnail: String?,
    val categoryId: Long,
    val discountPrice: Double? = 0.0,
    val rating: Double
)