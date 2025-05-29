package dev.mo.surfcart.product_details.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.product_details.usecase.GetProductByIdUseCase
import dev.mo.surfcart.product_details.usecase.GetProductDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getProductByIdiUseCase: GetProductByIdUseCase

) : ViewModel() {
    private var _productDetails : Map<String, String> = emptyMap()
    var productDetails = _productDetails
    private val _product = MutableStateFlow<Product?>(null)
    val product = _product


    suspend fun getProductDetails(productId: Long): Map<String, String> {
        return getProductDetailsUseCase.getProductDetails(productId)
    }
    suspend fun getProductById(productId: Long) {
        _product.value= getProductByIdiUseCase.getProductById(productId)
    }
}


