package dev.mo.surfcart.categories.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _parentCategories = MutableStateFlow<List<Category>>(emptyList())
    val parentCategories = _parentCategories.asStateFlow()

    private val _subCategories = MutableStateFlow<List<Category>>(emptyList())
    val subCategories = _subCategories.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()


    fun loadInitial(categoryId: Long) {
        viewModelScope.launch {
            _parentCategories.value = getCategoriesUseCase()
            val selectedParent = if (categoryId!=-1L){
                categoryId
            }
            else{
                _parentCategories.value.first().id.toLong()
            }
            _subCategories.value = getSubCategoriesUseCase(selectedParent)
            val firstSub = _subCategories.value.firstOrNull()
            if (firstSub != null) {
                loadProducts(firstSub.id.toLong())
            }
        }
    }

    fun loadSubCategories(parentId: Long) {
        viewModelScope.launch {
            _subCategories.value = getSubCategoriesUseCase(parentId)
            val firstSub = _subCategories.value.firstOrNull()
            if (firstSub != null) {
                loadProducts(firstSub.id.toLong())
            }
        }
    }

    fun loadProducts(categoryId: Long) {
        viewModelScope.launch {
            _products.value = getProductsUseCase.getProductsOfSupCategory(categoryId)
        }
    }
}