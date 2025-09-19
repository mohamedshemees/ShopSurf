package dev.mo.surfcart.account.data.dto

import dev.mo.surfcart.account.domain.repository.AccountRepository
import dev.mo.surfcart.checkout.ui.PaymentMethodItem
import dev.mo.surfcart.core.safeSupabaseCall
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : AccountRepository {
    override suspend fun getCustomerAddresses(): List<CustomerAddress> {
        return safeSupabaseCall {
                postgrest.rpc("get_customer_addresses")
                    .decodeList<CustomerAddress>()
            }
    }


    override suspend fun getPaymentMethods(): List<PaymentMethodItem> {
        return safeSupabaseCall {
            postgrest.rpc("get_payment_methods")
                .decodeList<PaymentMethodItem>()
        }
    }
}