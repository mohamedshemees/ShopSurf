package dev.mo.surfcart.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.home.usecase.GetOnSaleProductsUseCase
import dev.mo.surfcart.home.usecase.GetParentCategoriesUseCase
import dev.mo.surfcart.home.usecase.GetSearchedProducts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.listOf

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getParentCategories: GetParentCategoriesUseCase,
    private val getOnSaleProductsUseCase: GetOnSaleProductsUseCase,
    private val getSearchedProducts: GetSearchedProducts

):ViewModel() {

    private var _parentCategories = MutableStateFlow(listOf<Category>())
    val parentCategories = _parentCategories

    private var _banners = MutableStateFlow(listOf<String>())
    val banners = _banners

    private val _products = MutableStateFlow(listOf<Product>())
    val products= _products
    private var _onSaleProducts = MutableStateFlow(listOf<Product>())
    val onSaleProducts = _onSaleProducts

    private val queryFlow = MutableStateFlow("")


    init {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { term ->
                    val result = getSearchedProducts.getOnSaleProducts(term)
                    _products.update {  result}
                }
        }
        viewModelScope.launch {
            loadBanners()
            getParentCategories()
            getOnSaleProducts()
        }
    }

    fun onSearchChanged(query: String) {
        queryFlow.value = query
    }
    private suspend fun getOnSaleProducts() {
        _onSaleProducts.value = getOnSaleProductsUseCase.getOnSaleProducts()
    }
    private suspend fun getParentCategories() {
        _parentCategories.value = getParentCategories.invoke()
    }

    private suspend fun loadBanners() {
        _banners.value = getParentCategories.getBanners()
    }
}

