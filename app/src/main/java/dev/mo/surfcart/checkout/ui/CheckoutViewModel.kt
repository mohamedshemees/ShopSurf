package dev.mo.surfcart.checkout.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.domain.usecase.GetCustomerAddressUseCase
import dev.mo.surfcart.checkout.usecase.GetCheckoutProductsUseCase
import dev.mo.surfcart.checkout.usecase.GetPaymentMethodsUseCase
import dev.mo.surfcart.checkout.usecase.PlaceOrderUseCase
// PaymentMethodItem should be imported if it's not in this package.
// Assuming PaymentMethodItem is in dev.mo.surfcart.checkout.ui based on previous steps.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCustomerAddressUseCase: GetCustomerAddressUseCase,
    private val getCheckoutProductsUseCase: GetCheckoutProductsUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
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
            try {
                val addresses = getCustomerAddressUseCase()
                _uiState.value = _uiState.value.copy(userAddresses = addresses)
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
            try {
                val products = getCheckoutProductsUseCase()
                _uiState.value = _uiState.value.copy(checkoutProducts = products)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error loading products: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun updateAddress(addressId: UUID){
        _uiState.update { it.copy(selectedUserAddresses=addressId) }
    }
    fun updatePaymentMethod(paymentMethodId: UUID){
        _uiState.update { it.copy(selectedPaymentMethod=paymentMethodId) }
    }
     fun onPlaceOrderCLicked(){
         viewModelScope.launch {
             Log.d("WOWTEST","place order clicked")
             Log.d("WOWTEST","${_uiState.value.selectedUserAddresses}")

             placeOrderUseCase(
                 addressId = _uiState.value.selectedUserAddresses!!,
                 paymentMethodId = _uiState.value.selectedPaymentMethod!!,
             )
         }
    }
    private fun loadPaymentMethods() {
        viewModelScope.launch {

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
