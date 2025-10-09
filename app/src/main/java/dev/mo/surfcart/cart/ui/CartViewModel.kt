package dev.mo.surfcart.cart.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.cart.CartItem
import dev.mo.surfcart.cart.usecase.AddProductToCartUseCase
import dev.mo.surfcart.cart.usecase.DecreaseQuantityUseCase
import dev.mo.surfcart.cart.usecase.GetCartItemsUseCase
import dev.mo.surfcart.cart.usecase.IncreaseQuantityUseCase
import dev.mo.surfcart.cart.usecase.RemoveProductFromCartUseCase
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.core.DuplicateEntryException
import dev.mo.surfcart.core.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val increaseQuantityUseCase: IncreaseQuantityUseCase,
    private val decreaseQuantityUseCase: DecreaseQuantityUseCase,
) : BaseViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    init {
        getCartItems()
    }

    private fun getCartItems() {
        tryToExecute(
            call = { getCartItemsUseCase() },
            onSuccess = { items ->
                _cartItems.value = items
            },
            onError = { exception ->
                sendUiEvent(UiEvent.ShowErrorSnackbar(exception.message ?: "Couldn't load cart"))
            }
        )
    }

    fun addToCart(productId: Int) {
        tryToExecute(
            call = { addProductToCartUseCase(productId) },
            onSuccess = {
                getCartItems()
                sendUiEvent(UiEvent.ShowSuccessSnackbar("Item added successfully"))
            },
            onError = { exception ->
                val errorMessage = if (exception is DuplicateEntryException) {
                    "Item is already in cart"
                } else {
                    exception.message ?: "Couldn't add item"
                }
                sendUiEvent(UiEvent.ShowErrorSnackbar(errorMessage))
            }
        )
    }

    fun removeFromCart(productId: Int) {
        tryToExecute(
            call = { removeProductFromCartUseCase(productId) },
            onSuccess = { getCartItems() },
            onError = { exception ->
                sendUiEvent(UiEvent.ShowErrorSnackbar(exception.message ?: "Couldn't remove item"))
            }
        )
    }

    fun increaseQuantity(productId: Int) {
        tryToExecute(
            call = { increaseQuantityUseCase(productId) },
            onSuccess = { getCartItems() },
            onError = { exception ->
                sendUiEvent(UiEvent.ShowErrorSnackbar(exception.message ?: "An error occurred"))
            }
        )
    }

    fun decreaseQuantity(productId: Int) {
        tryToExecute(
            call = { decreaseQuantityUseCase(productId) },
            onSuccess = { getCartItems() },
            onError = { exception ->
                sendUiEvent(UiEvent.ShowErrorSnackbar(exception.message ?: "An error occurred"))
            }
        )
    }
}