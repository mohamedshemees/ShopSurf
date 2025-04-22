package dev.mo.surfcart.product_details.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.ProductDetails
import dev.mo.surfcart.product_details.usecase.GetProductDetails
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val GetProductDetailsUseCase: GetProductDetails

) : ViewModel() {
    private val _productDetails = MutableStateFlow(mapOf<String, String>())
    val productDetails = _productDetails


    suspend fun getProductDetails(productId: Long) {
        _productDetails.value = GetProductDetailsUseCase.getProductDetails(productId)
    }
}


