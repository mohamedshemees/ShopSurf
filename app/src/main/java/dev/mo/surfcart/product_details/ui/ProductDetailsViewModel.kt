package dev.mo.surfcart.product_details.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.product_details.usecase.GetProductByIdUseCase
import dev.mo.surfcart.product_details.usecase.GetProductDetailsUseCase
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getProductByIdiUseCase: GetProductByIdUseCase,
    private val getProductsBySubCategoryUseCase: GetProductsBySubCategoryUseCase
) : ViewModel() {
    private var _productDetails: Map<String, String> = emptyMap()
    var productDetails = _productDetails
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _similarProducts = MutableStateFlow<List<Product>>(emptyList())
    val similarProducts: StateFlow<List<Product>> = _similarProducts

    suspend fun getProductDetails(productId: Long): Map<String, String> {

        productDetails = getProductDetailsUseCase.getProductDetails(productId)
        return productDetails
    }

    suspend fun getProductById(productId: Long) {
        _product.value = getProductByIdiUseCase.getProductById(productId)
    }


    suspend fun fetchSimilarProducts() {
        _similarProducts.value = getProductsBySubCategoryUseCase
            .getProductsOfSupCategory(product.value!!.categoryId)

    }
}
