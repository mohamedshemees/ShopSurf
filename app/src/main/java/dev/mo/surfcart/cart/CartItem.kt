package dev.mo.surfcart.cart

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(

    @SerialName("product_id")
    val productId: Int,
    @SerialName("product_name")
    val title: String,
    @SerialName("product_description")
    val description: String,
    @SerialName("product_price")
    val price: Double,
    @SerialName("product_thumbnail")
    val imageUrl: String,
    @SerialName("product_quantity")
    val quantity: Int,
)
