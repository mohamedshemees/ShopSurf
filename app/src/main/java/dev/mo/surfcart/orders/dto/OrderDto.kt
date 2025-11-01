package dev.mo.surfcart.orders.dto

import dev.mo.surfcart.order_tracking.Order
import dev.mo.surfcart.order_tracking.OrderStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class OrderDto(
    @SerialName("order_id")
    val orderId: String,
    @SerialName("order_date")
    val orderDate: String, // Assuming ISO 8601 format from Supabase
    @SerialName("address")
    val address: String,
    @SerialName("status")
    val status: String
)

fun OrderDto.toOrder(): Order {
    // Supabase timestamp with timezone format
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault())
    val date = try {
        dateFormat.parse(orderDate)
    } catch (e: Exception) {
        Date() // Fallback to current date on parsing error
    }

    val orderStatus = try {
        OrderStatus.valueOf(status.uppercase())
    } catch (e: IllegalArgumentException) {
        OrderStatus.PENDING // Fallback to PENDING if status is unknown
    }

    return Order(
        orderId = this.orderId,
        orderDate = date,
        address = this.address,
        status = orderStatus
    )
}
