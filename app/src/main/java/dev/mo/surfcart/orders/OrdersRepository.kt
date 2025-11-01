package dev.mo.surfcart.orders

import dev.mo.surfcart.order_tracking.Order

interface OrdersRepository {
    suspend fun getOrders(): List<Order>
}
