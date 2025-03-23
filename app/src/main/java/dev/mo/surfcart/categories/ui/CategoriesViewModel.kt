package dev.mo.surfcart.categories.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.categories.usecase.GetProductsUseCase
import dev.mo.surfcart.categories.usecase.GetSupCategoriesUseCase
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.home.usecase.GetParentCategoriesUseCase
import dev.mo.surfcart.products.usecase.GetProductsBySubCategoryUseCase
import dev.mo.surfcart.products.usecase.GetSubCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetParentCategoriesUseCase,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase,
    private val getProductsUseCase: GetProductsBySubCategoryUseCase
) : ViewModel() {

    private val _Parentcategories = MutableStateFlow<List<Category>>(emptyList())
    val parentCategories = _Parentcategories.asStateFlow()

    private val _subCategories = MutableStateFlow<List<Category>>(emptyList())
    val subCategories = _subCategories.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()


    init {
        loadParentCategories()
        loadSubCategories(1)
        loadProducts(3)
    }

    fun loadParentCategories() {
        viewModelScope.launch {
            _Parentcategories.value = getCategoriesUseCase.invoke()
        }
    }
    fun loadSubCategories(parentId: Long) {
        viewModelScope.launch {
            _subCategories.value = getSubCategoriesUseCase.invoke(parentId)
        }
    }
    fun loadProducts(categoryId: Long) {
        viewModelScope.launch {
            _products.value = getProductsUseCase.getProductsOfSupCategory(categoryId)
        }
    }

}