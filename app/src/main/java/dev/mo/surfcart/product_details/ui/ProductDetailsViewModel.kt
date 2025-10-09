package dev.mo.surfcart.product_details.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.product_details.usecase.GetProductByIdUseCase
import dev.mo.surfcart.product_details.usecase.GetProductDetailsUseCase
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getProductByIdiUseCase: GetProductByIdUseCase,
    private val getProductsBySubCategoryUseCase: GetProductsBySubCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProductData(productId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val productData = getProductByIdiUseCase.getProductById(productId)
                val detailsMap = getProductDetailsUseCase.getProductDetails(productId)

                _uiState.update {
                    it.copy(
                        product = productData,
                        productDetails = detailsMap
                    )
                }

                if (productData != null) {
                    val similar = getProductsBySubCategoryUseCase.getProductsOfSupCategory(productData.categoryId)
                    _uiState.update { it.copy(similarProducts = similar) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to load product details: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
