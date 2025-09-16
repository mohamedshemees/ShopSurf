package dev.mo.surfcart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkIfLoggedInUseCase: CheckIfLoggedInUseCase
) : ViewModel() {
    val _state = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val state = _state.asStateFlow()

    init {

        viewModelScope.launch {
            val loggedIn = checkIfLoggedInUseCase()
            _state.value = if (loggedIn) MainUiState.ShowHome else MainUiState.ShowLogin
        }
    }

    sealed class MainUiState {
        object Loading : MainUiState()
        object ShowHome : MainUiState()
        object ShowLogin : MainUiState()
    }
}