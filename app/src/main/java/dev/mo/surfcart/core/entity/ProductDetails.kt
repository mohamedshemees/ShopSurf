package dev.mo.surfcart.core.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetails(
    @SerialName("product_id")
    val productId: Long = 0,
    @SerialName("category_id")
    val categoryId: Long = 0,
    @SerialName("category_name")
    val categoryName: String = "0",
    @SerialName("properties")
    val properties: Map<String, String>
)
