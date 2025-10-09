package dev.mo.surfcart.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    protected fun <T> tryToExecute(
        call: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (exception: DomainException) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = call()
                onSuccess(result)
            } catch (e: DomainException) {
                Log.e("BaseViewModel", "Caught DomainException: ${e.message}")
                onError(e)
            }
        }
    }

    protected fun tryToExecute(
        call: suspend () -> Unit,
        onSuccess: () -> Unit,
        onError: (exception: DomainException) -> Unit
    ) {
        viewModelScope.launch {
            try {
                call()
                onSuccess()
            } catch (e: DomainException) {
                Log.e("BaseViewModel", "Caught DomainException: ${e.message}")
                onError(e)
            }
        }
    }

    protected fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}