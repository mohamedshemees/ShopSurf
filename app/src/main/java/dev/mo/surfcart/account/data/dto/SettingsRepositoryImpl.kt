package dev.mo.surfcart.account.data.dto

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.mo.surfcart.account.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {


    val appTheme: Flow<Boolean> = dataStore.data
        .map { it[DARK_MODE] ?: false }

    override suspend  fun enableDarkMode() {
        dataStore.edit { prefs ->
            prefs[DARK_MODE] = !(prefs[DARK_MODE] ?: false)
        }
    }
    override fun getCurrentTheme() = appTheme

    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode_enabled")
    }
}