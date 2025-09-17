package dev.mo.surfcart.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mo.surfcart.account.domain.usecase.LogoutUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    val logoutUseCase: LogoutUseCase
) : ViewModel() {

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}