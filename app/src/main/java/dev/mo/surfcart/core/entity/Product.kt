package dev.mo.surfcart.core.entity

data class Product(
    val productId: Long,               // bigint, auto-incremented, not null
    val timeCreated: String? = null,   // time, nullable, defaults to now() in DB
    val sellerId: String,                // bigint, not null
    val productName: String,           // text, not null
    val productDescription: String,    // text, not null (fixed typo in "descreption")
    val modelName: String? = "Generic",// text, nullable, defaults to "Generic"
    val productPrice: Double?,         // double precision, nullable
    val productCount: Long?,           // bigint, nullable
    val productThumbnail: String?,     // text, nullable
    val categoryId: Long,              // bigint, not null
    val discountPrice: Double? = 0.0,  // double precision, nullable, defaults to 0
    val rating: Double                 // double precision, not null
)