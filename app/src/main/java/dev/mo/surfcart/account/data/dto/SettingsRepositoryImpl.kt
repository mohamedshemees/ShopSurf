package dev.mo.surfcart.account.data.dto

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dev.mo.surfcart.account.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {


     val appTheme: Flow<Boolean> = dataStore.data
        .map { it[DARK_MODE] ?: false }

    override suspend fun enableDarkMode():Flow<Boolean> {
        dataStore.edit { prefs ->
            prefs[DARK_MODE] = !(prefs[DARK_MODE] ?: false)
        }
        return appTheme
    }
    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode_enabled")
    }
}