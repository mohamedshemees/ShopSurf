package dev.mo.surfcart.account.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.domain.repository.SettingsRepository
import dev.mo.surfcart.account.domain.usecase.GetUserDataUseCase
import dev.mo.surfcart.account.domain.usecase.LogoutUseCase
import dev.mo.surfcart.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val settingsRepository: SettingsRepository,

    ) : BaseViewModel() {
        private val _accountUiState = MutableStateFlow<AccountUiState>(AccountUiState.Loading)
        val accountUiState = _accountUiState.asStateFlow()
    init {
        loadUserDate()
    }
    val isDarkMode =
        settingsRepository.getCurrentTheme().stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun enableDarkMode() {
        viewModelScope.launch {
            settingsRepository.enableDarkMode()
        }
    }
    private fun loadUserDate() {
        tryToExecute(
            call = { getUserDataUseCase() },
            onSuccess = { userdata->
                _accountUiState.value = AccountUiState.Ready(
                    userName = userdata
                )
            },
            onError ={}
        )
    }
    fun onLogoutClicked() {
        tryToExecute(call = { logoutUseCase() }, onSuccess = {}, onError = { exception -> })
    }
}
sealed class AccountUiState {
    object Loading : AccountUiState()
    object Error : AccountUiState()
    data class Ready(
        val userName:String,
    ) : AccountUiState()

}