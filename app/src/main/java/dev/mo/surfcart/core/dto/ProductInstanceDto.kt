package dev.mo.surfcart.core.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductInstanceDto(
    @SerialName("product_id")
    val productId: Long,

    @SerialName("time_created")
    val timeCreated: String? = null,

    @SerialName("seller_id")
    val sellerId: String,

    @SerialName("product_name")
    val productName: String,

    @SerialName("product_description")
    val productDescription: String,

    @SerialName("model_name")
    val modelName: String? = "Generic",

    @SerialName("product_price")
    val productPrice: Double?,

    @SerialName("product_count")
    val productCount: Long?,

    @SerialName("product_thumbnail")
    val productThumbnail: String?,

    @SerialName("category_id")
    val categoryId: Long,

    @SerialName("discount_price")
    val discountPrice: Double? = 0.0,

    @SerialName("rating")
    val rating: Double
)