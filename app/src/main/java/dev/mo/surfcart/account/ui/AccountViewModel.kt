package dev.mo.surfcart.account.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.domain.repository.SettingsRepository
import dev.mo.surfcart.account.domain.usecase.LogoutUseCase
import dev.mo.surfcart.core.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val settingsRepository: SettingsRepository,

    ) : BaseViewModel() {

    fun enableDArkmoe() {
        viewModelScope.launch {
            settingsRepository.enableDarkMode()
        }
    }

    fun onLogoutClicked() {
        tryToExecute(call = { logoutUseCase() }, onSuccess = {
            Log.d("AccountViewModel", "Logout successful")
        }, onError = { exception ->
            Log.e("AccountViewModel", "Logout failed: ${exception.message}", exception)
        })
    }
}