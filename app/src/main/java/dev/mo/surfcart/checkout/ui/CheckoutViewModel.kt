package dev.mo.surfcart.checkout.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.domain.usecase.GetCustomerAddressUseCase
import dev.mo.surfcart.checkout.usecase.GetCheckoutProductsUseCase
import dev.mo.surfcart.checkout.usecase.GetPaymentMethodsUseCase
import dev.mo.surfcart.checkout.usecase.PlaceOrderUseCase
import dev.mo.surfcart.core.BaseViewModel
import dev.mo.surfcart.core.DomainException
import dev.mo.surfcart.core.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCustomerAddressUseCase: GetCustomerAddressUseCase,
    private val getCheckoutProductsUseCase: GetCheckoutProductsUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CheckOutUiState())
    val uiState: StateFlow<CheckOutUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.update { it.copy(isLoading = true) }
        loadUserAddresses()
        loadCheckoutProducts()
        loadPaymentMethods()
    }

    private fun loadUserAddresses() {
        tryToExecute(
            call = { getCustomerAddressUseCase() },
            onSuccess = { addresses -> _uiState.update { it.copy(userAddresses = addresses) } },
            onError = { exception -> handleException(exception) }
        )
    }

    private fun loadCheckoutProducts() {
        tryToExecute(
            call = { getCheckoutProductsUseCase() },
            onSuccess = { products -> _uiState.update { it.copy(checkoutProducts = products) } },
            onError = { exception -> handleException(exception) }
        )
    }

    private fun loadPaymentMethods() {
        tryToExecute(
            call = { getPaymentMethodsUseCase() },
            onSuccess = { methods -> _uiState.update { it.copy(paymentMethods = methods, isLoading = false) } },
            onError = { exception -> handleException(exception) }
        )
    }

    fun updateAddress(addressId: UUID) {
        _uiState.update { it.copy(selectedUserAddresses = addressId) }
    }

    fun updatePaymentMethod(paymentMethodId: UUID) {
        _uiState.update { it.copy(selectedPaymentMethod = paymentMethodId) }
    }

    fun onPlaceOrderClicked() {
        val currentState = _uiState.value
        val addressId = currentState.selectedUserAddresses
        val paymentMethodId = currentState.selectedPaymentMethod

        if (addressId == null || paymentMethodId == null) {
            sendUiEvent(UiEvent.ShowErrorSnackbar("Please select an address and payment method."))
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        tryToExecute(
            call = { placeOrderUseCase(addressId, paymentMethodId) },
            onSuccess = {
                _uiState.update { it.copy(isLoading = false, orderPlacedSuccessfully = true) }
                sendUiEvent(UiEvent.ShowSuccessSnackbar("Order placed successfully!"))
            },
            onError = { exception -> handleException(exception) }
        )
    }

    private fun handleException(exception: DomainException) {
        _uiState.update { it.copy(isLoading = false) }
        sendUiEvent(UiEvent.ShowErrorSnackbar(exception.message ?: "An unknown error occurred"))
    }
}