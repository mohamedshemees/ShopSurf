package dev.mo.surfcart.categories.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.core.DomainException
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.home.usecase.GetParentCategoriesUseCase
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import dev.mo.surfcart.products.usecase.GetSubCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetParentCategoriesUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val getProductsUseCase: GetProductsBySubCategoryUseCase
) : BaseViewModel() {

    private val _parentCategories = MutableStateFlow<List<Category>>(emptyList())
    val parentCategories = _parentCategories.asStateFlow()

    private val _subCategories = MutableStateFlow<List<Category>>(emptyList())
    val subCategories = _subCategories.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadInitial(categoryId: Long) {
        _isLoading.value = true
        tryToExecute(
            call = {
                val parentCategories = getCategoriesUseCase()
                val selectedParentId = if (categoryId != -1L) categoryId else parentCategories.first().id.toLong()
                val subCategories = getSubCategoriesUseCase(selectedParentId)
                Triple(parentCategories, subCategories, subCategories.firstOrNull())
            },
            onSuccess = { (parents, subs, firstSub) ->
                _parentCategories.value = parents
                _subCategories.value = subs
                if (firstSub != null) {
                    loadProducts(firstSub.id.toLong())
                } else {
                    _products.value = emptyList()
                    _isLoading.value = false
                }
            },
            onError = { exception -> handleException(exception) }
        )
    }

    fun loadSubCategories(parentId: Long) {
        _isLoading.value = true
        tryToExecute(
            call = { getSubCategoriesUseCase(parentId) },
            onSuccess = { subs ->
                _subCategories.value = subs
                val firstSub = subs.firstOrNull()
                if (firstSub != null) {
                    loadProducts(firstSub.id.toLong())
                } else {
                    _products.value = emptyList()
                    _isLoading.value = false
                }
            },
            onError = { exception -> handleException(exception) }
        )
    }

    fun loadProducts(categoryId: Long) {
        _isLoading.value = true
        tryToExecute(
            call = { getProductsUseCase.getProductsOfSupCategory(categoryId) },
            onSuccess = { productsResult ->
                _products.value = productsResult
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