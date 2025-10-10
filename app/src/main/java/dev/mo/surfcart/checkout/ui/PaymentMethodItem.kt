package dev.mo.surfcart.checkout.ui

import dev.mo.surfcart.account.data.dto.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class PaymentMethodItem(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val type: String,

    @SerialName("brand")
    val provider: String? = null,

    val last4: String? = null,

    @SerialName("exp_month")
    val expiryMonth: Int? = null,

    @SerialName("exp_year")
    val expiryYear: Int? = null,

    val email: String? = null,

    @SerialName("is_default")
    val isDefault: Boolean,

    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
enum class PaymentType {
    @SerialName("card")
    Card,

    @SerialName("paypal")
    Paypal,

    @SerialName("apple_pay")
    ApplePay,

    @SerialName("google_pay")
    GooglePay
}