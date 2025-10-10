package dev.mo.surfcart.product_details.ui

import dev.mo.surfcart.core.entity.Product

data class ProductDetailsUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val productDetails: Map<String, String> = emptyMap(),
    val similarProducts: List<Product> = emptyList(),
    val errorMessage: String? = null
)
