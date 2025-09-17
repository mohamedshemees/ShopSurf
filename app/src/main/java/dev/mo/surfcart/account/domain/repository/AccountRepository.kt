package dev.mo.surfcart.account.domain.repository

import dev.mo.surfcart.account.data.dto.CustomerAddress

interface AccountRepository {
    suspend fun getCustomerAddresses(): List<CustomerAddress>
}