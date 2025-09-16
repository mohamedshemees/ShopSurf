package dev.mo.surfcart.registration.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class RegistrationBoardingViewModel : ViewModel() {

    // Define the roles
    enum class UserRole {
        SELLER, CUSTOMER
    }

    // Backing property for state
    private val _selectedRole = MutableStateFlow<UserRole?>(null)
    val selectedRole: StateFlow<UserRole?> get() = _selectedRole

    // Update selected role
    fun selectRole(role: UserRole) {
        viewModelScope.launch {
            _selectedRole.emit(role)
        }
    }

    fun isRoleSelected(): Boolean = _selectedRole.value != null
}