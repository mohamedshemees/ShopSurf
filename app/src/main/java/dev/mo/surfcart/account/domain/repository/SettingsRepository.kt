package dev.mo.surfcart.account.domain.repository

import dev.mo.surfcart.account.data.dto.CustomerAddress
import dev.mo.surfcart.checkout.ui.PaymentMethodItem
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface SettingsRepository {
    suspend fun enableDarkMode() : Flow<Boolean>
}