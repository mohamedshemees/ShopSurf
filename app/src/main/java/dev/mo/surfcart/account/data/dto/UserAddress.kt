package dev.mo.surfcart.account.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerAddress(
    val street: String,
    val city: String,
    val country: String,
    @SerialName("postal_code")
    val postalCode: String? = null,
    @SerialName("is_default")
    val isDefault: Boolean = false
)