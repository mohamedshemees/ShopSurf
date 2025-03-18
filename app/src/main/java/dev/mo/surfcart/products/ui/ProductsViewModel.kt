package dev.mo.surfcart.products.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import dev.mo.surfcart.products.usecase.GetSubCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel@Inject constructor(
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val getProductsUseCase: GetProductsBySubCategoryUseCase


): ViewModel(){


    var parentCategory =-1L

    private var _products= MutableStateFlow(listOf<Product>())
    val products = _products


    private val _categories = MutableStateFlow(listOf<Category>())
    val categories= _categories

    private val _selectedCategoryId = MutableStateFlow(-1L) // Default to "All"
    val selectedCategoryId= _selectedCategoryId

    init {
       loadCategories(parentCategory)
        loadProducts(parentCategory) // Initial load: all products
    }

     fun loadCategories(parentId: Long) {
        viewModelScope.launch {
            _categories.value = getSubCategoriesUseCase.invoke(parentId)
        }
    }

    fun selectCategory(categoryId: Long) {
        _selectedCategoryId.value = categoryId
        loadProducts(categoryId)
    }

     fun loadProducts(categoryId: Long) {
        viewModelScope.launch {
            _products.value = getProductsUseCase.invoke(categoryId)
            Log.d("wow","loadproducts viewmodel ")
        }
    }
}

