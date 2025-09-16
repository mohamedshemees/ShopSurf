package dev.mo.surfcart.products.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import dev.mo.surfcart.products.usecase.GetSubCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsBySubCategoryUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase
) : ViewModel() {
    private var _products = MutableStateFlow(listOf<Product>())
    val products = _products
    private var _filteredProducts = MutableStateFlow(listOf<Product>())
    val filteredProducts = _filteredProducts

    private val _categories = MutableStateFlow(listOf<Category>())
    val categories = _categories

    private val _selectedCategoryId = MutableStateFlow(-1L) // Default to "All"
    val selectedCategoryId = _selectedCategoryId

    var parentCategory = -1L

    fun loadCategories(parentId: Long) {

        viewModelScope.launch {
            _categories.value = getSubCategoriesUseCase.invoke(parentId)
            _products.value = getProductsUseCase.invoke(parentCategory)

        }
    }

    fun selectCategory(categoryId: Long) {
        _selectedCategoryId.value = categoryId
        viewModelScope.launch {
            if (categoryId == -1L) {
                _products.value = getProductsUseCase.invoke(parentCategory)
            } else {
                _products.value = getProductsUseCase.getProductsOfSupCategory(categoryId)
            }
        }
    }

}

