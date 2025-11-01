package dev.mo.surfcart.order_tracking

import java.util.Date

data class Order(
    val orderId: String,
    val orderDate: Date,
    val address: String,
    val status: OrderStatus
)

enum class OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED
}