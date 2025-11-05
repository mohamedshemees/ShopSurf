package dev.mo.surfcart.account.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
     suspend fun enableDarkMode()
     fun getCurrentTheme(): Flow<Boolean>
}