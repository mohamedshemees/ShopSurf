package dev.mo.surfcart.core.dto

import dev.mo.surfcart.core.entity.Product

object ProductMapper{
    fun ProductDto.toProduct(): Product {
        return Product(
            productId =  productId,
            timeCreated = timeCreated,
            sellerId = sellerId,
            productName = productName,
            productDescription = productDescription,
            modelName = modelName,
            productPrice = productPrice,
            productCount = productCount,
            productThumbnail = productThumbnail,
            categoryId = categoryId,
            discountPrice = discountPrice,
            rating = rating
        )
    }
}