package dev.mo.surfcart.account.ui

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.domain.repository.SettingsRepository
import dev.mo.surfcart.account.domain.usecase.LogoutUseCase
import dev.mo.surfcart.core.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val settingsRepository: SettingsRepository,

    ) : BaseViewModel() {
    val isDarkMode =
        settingsRepository.getCurrentTheme().stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun enableDarkMode() {
        viewModelScope.launch {
            settingsRepository.enableDarkMode()
        }
    }

    fun onLogoutClicked() {
        tryToExecute(call = { logoutUseCase() }, onSuccess = {}, onError = { exception -> })
    }
}