package dev.mo.surfcart.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.cart.CartItem
import dev.mo.surfcart.cart.usecase.AddProductToCartUseCase
import dev.mo.surfcart.cart.usecase.DecreaseQuantityUseCase
import dev.mo.surfcart.cart.usecase.GetCartItemsUseCase
import dev.mo.surfcart.cart.usecase.IncreaseQuantityUseCase
import dev.mo.surfcart.cart.usecase.RemoveProductFromCartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,

    private val increaseQuantityUseCase: IncreaseQuantityUseCase,
    private val decreaseQuantityUseCase: DecreaseQuantityUseCase,
) : ViewModel() {

    private val _cartItems= MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems= _cartItems

    init {
        viewModelScope.launch {
            getCartItems()
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            addProductToCartUseCase(productId)
            getCartItems()
        }
    }

    suspend fun clearCart() {
        // Implement clear cart functionality if needed
    }

    suspend fun getTotalPrice(): Double {
        // Implement total price calculation if needed
        return 0.0 // Placeholder
    }

     fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            removeProductFromCartUseCase(productId)
            getCartItems()
        }
    }

     fun increaseQuantity(productId: Int) {
         viewModelScope.launch {
             increaseQuantityUseCase(productId)
             getCartItems()
         }
    }

     fun decreaseQuantity(productId: Int) {
         viewModelScope.launch {
             decreaseQuantityUseCase(productId)
             getCartItems()
         }
    }
    private suspend fun getCartItems() {
         _cartItems.value=getCartItemsUseCase()
    }

}