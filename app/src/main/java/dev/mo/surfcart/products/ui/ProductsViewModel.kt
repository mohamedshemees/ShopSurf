package dev.mo.surfcart.products.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.core.DomainException
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import dev.mo.surfcart.products.usecase.GetSubCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsBySubCategoryUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase
) : BaseViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow(-1L)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

     var parentCategory = -1L

    fun loadCategories(parentId: Long) {
        parentCategory = parentId
        _isLoading.value = true

        tryToExecute(
            call = {
                val subCategories = getSubCategoriesUseCase(parentId)
                val allProducts = getProductsUseCase(parentCategory)
                Pair(subCategories, allProducts)
            },
            onSuccess = { (subCategories, allProducts) ->
                _categories.value = subCategories
                _products.value = allProducts
                _isLoading.value = false
            },
            onError = { exception -> handleException(exception) }
        )
    }

    fun selectCategory(categoryId: Long) {
        _selectedCategoryId.value = categoryId
        _isLoading.value = true

        tryToExecute(
            call = {
                if (categoryId == -1L) {
                    getProductsUseCase(parentCategory)
                } else {
                    getProductsUseCase.getProductsOfSupCategory(categoryId)
                }
            },
            onSuccess = { productResult ->
                _products.value = productResult
                _isLoading.value = false
            },
            onError = { exception -> handleException(exception) }
        )
    }

    private fun handleException(exception: DomainException) {
        _isLoading.value = false
        _error.value = exception.message
    }

    fun onErrorShown() {
        _error.value = null
    }
}