package dev.mo.surfcart.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.home.usecase.GetOnSaleProductsUseCase
import dev.mo.surfcart.home.usecase.GetParentCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getParentCategories: GetParentCategoriesUseCase,
    private val getOnSaleProductsUseCase: GetOnSaleProductsUseCase

):ViewModel() {

    private var _parentCategories = MutableStateFlow(listOf<Category>())
    val parentCategories = _parentCategories

    private var _banners = MutableStateFlow(listOf<String>())
    val banners = _banners

    private var _onSaleProducts = MutableStateFlow(listOf<Product>())
    val onSaleProducts = _onSaleProducts

    init {
        viewModelScope.launch {
            getParentCategories()
            getOnSaleProducts()
            loadBanners()
        }

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

