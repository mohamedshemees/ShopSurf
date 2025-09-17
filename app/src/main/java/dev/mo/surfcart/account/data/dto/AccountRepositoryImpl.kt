package dev.mo.surfcart.account.data.dto

import dev.mo.surfcart.account.domain.repository.AccountRepository
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : AccountRepository {
    override suspend fun getCustomerAddresses(): List<CustomerAddress> {
        return withContext(Dispatchers.IO)
        {
            postgrest.rpc("get_customer_addresses")
                .decodeList<CustomerAddress>()
        }
    }
}