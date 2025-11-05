package dev.mo.surfcart

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkIfLoggedInUseCase: CheckIfLoggedInUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val _state = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val state = _state.asStateFlow()

    init {

        viewModelScope.launch {
            val loggedIn = checkIfLoggedInUseCase()
            _state.update {
                if (loggedIn) MainUiState.ShowHome
                else MainUiState.ShowLogin
            }
        }
        viewModelScope.launch {
            settingsRepository.getCurrentTheme().collect { isDarkTheme ->
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        }

    sealed class MainUiState {
        object Loading : MainUiState()
        object ShowHome : MainUiState()
        object ShowLogin : MainUiState()
        data class UpdateAppTheme(val theme: Boolean) : MainUiState()
    }
}