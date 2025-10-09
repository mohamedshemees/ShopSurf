package dev.mo.surfcart.product_details.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.product_details.usecase.GetProductByIdUseCase
import dev.mo.surfcart.product_details.usecase.GetProductDetailsUseCase
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getProductsBySubCategoryUseCase: GetProductsBySubCategoryUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProductData(productId: Long) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        tryToExecute(
            call = {
                val productData = getProductByIdUseCase.getProductById(productId)
                val detailsMap = getProductDetailsUseCase.getProductDetails(productId)
                val similarProducts = productData?.let {
                    getProductsBySubCategoryUseCase.getProductsOfSupCategory(it.categoryId)
                } ?: emptyList()

                Triple(productData, detailsMap, similarProducts)
            },
            onSuccess = { (product, details, similar) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        product = product,
                        productDetails = details,
                        similarProducts = similar
                    )
                }
            },
            onError = { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load product details: ${exception.message}"
                    )
                }
            }
        )
    }

    fun onErrorShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}