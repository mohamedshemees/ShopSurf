package dev.mo.surfcart.checkout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.data.dto.CustomerAddress
import dev.mo.surfcart.account.domain.usecase.GetCustomerAddressUseCase
import dev.mo.surfcart.cart.CartItem
import dev.mo.surfcart.checkout.usecase.GetCheckoutProductsUseCase
import dev.mo.surfcart.checkout.usecase.GetPaymentMethodsUseCase
// PaymentMethodItem should be imported if it's not in this package.
// Assuming PaymentMethodItem is in dev.mo.surfcart.checkout.ui based on previous steps.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckOutUiState(
    val userAddresses: List<CustomerAddress> = emptyList(),
    val checkoutProducts: List<CartItem> = emptyList(),
    val paymentMethods: List<PaymentMethodItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCustomerAddressUseCase: GetCustomerAddressUseCase,
    private val getCheckoutProductsUseCase: GetCheckoutProductsUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckOutUiState())
    val uiState: StateFlow<CheckOutUiState> = _uiState.asStateFlow()

    init {
        loadUserAddresses()
        loadCheckoutProducts()
        loadPaymentMethods()
    }

    private fun loadUserAddresses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val addresses = getCustomerAddressUseCase()
                _uiState.value = _uiState.value.copy(userAddresses = addresses, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error loading addresses: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private fun loadCheckoutProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val products = getCheckoutProductsUseCase()
                _uiState.value = _uiState.value.copy(checkoutProducts = products, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error loading products: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private fun loadPaymentMethods() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val methods = getPaymentMethodsUseCase()
                _uiState.value = _uiState.value.copy(paymentMethods = methods, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error loading payment methods: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
}
