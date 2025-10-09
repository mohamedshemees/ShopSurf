package dev.mo.surfcart.home.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.core.DomainException
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.home.usecase.GetOnSaleProductsUseCase
import dev.mo.surfcart.home.usecase.GetParentCategoriesUseCase
import dev.mo.surfcart.home.usecase.GetSearchedProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getParentCategoriesUseCase: GetParentCategoriesUseCase,
    private val getOnSaleProductsUseCase: GetOnSaleProductsUseCase,
    private val getSearchedProducts: GetSearchedProducts
) : BaseViewModel() {

    private val _parentCategories = MutableStateFlow<List<Category>>(emptyList())
    val parentCategories = _parentCategories.asStateFlow()

    private val _banners = MutableStateFlow<List<String>>(emptyList())
    val banners = _banners.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _onSaleProducts = MutableStateFlow<List<Product>>(emptyList())
    val onSaleProducts = _onSaleProducts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        setupSearch()
        loadInitialData()
    }

    fun loadInitialData() {
        _isLoading.value = true
        getOnSaleProducts()
        getParentCategories()
        getBanners()
    }

    private fun setupSearch() {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { term ->
                    if (term.isNotBlank()) {
                        searchProducts(term)
                    } else {
                        _products.value = emptyList()
                    }
                }
        }
    }

    private fun searchProducts(term: String) {
        tryToExecute(
            call = { getSearchedProducts.getOnSaleProducts(term) },
            onSuccess = { result -> _products.value = result },
            onError = { exception -> handleException(exception) }
        )
    }

    fun onSearchChanged(query: String) {
        queryFlow.value = query
    }

    private fun getOnSaleProducts() {
        tryToExecute(
            call = { getOnSaleProductsUseCase.getOnSaleProducts() },
            onSuccess = { productsResult -> _onSaleProducts.value = productsResult },
            onError = { exception -> handleException(exception) }
        )
    }

    private fun getParentCategories() {
        tryToExecute(
            call = { getParentCategoriesUseCase() },
            onSuccess = { categoriesResult -> _parentCategories.value = categoriesResult },
            onError = { exception -> handleException(exception) }
        )
    }

    private fun getBanners() {
        tryToExecute(
            call = { getParentCategoriesUseCase.getBanners() },
            onSuccess = { bannersResult ->
                _banners.value = bannersResult
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