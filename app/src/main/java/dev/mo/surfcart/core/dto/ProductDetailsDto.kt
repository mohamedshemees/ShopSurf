package dev.mo.surfcart.core.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailsDto(
    @SerialName("product_id")
    val productId: Long,
    @SerialName("category_id")
    val categoryId: Long,
    @SerialName("category_name")
    val categoryName: String,
    @SerialName("property_id")
    val propertyId: Int,
    @SerialName("property_value")
    val propertyValue: String,
    @SerialName("property_name")
    val propertyName: String
)
