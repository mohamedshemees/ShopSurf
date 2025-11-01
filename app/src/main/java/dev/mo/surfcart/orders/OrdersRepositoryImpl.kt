package dev.mo.surfcart.orders

import dev.mo.surfcart.core.safeSupabaseCall
import dev.mo.surfcart.order_tracking.Order
import dev.mo.surfcart.orders.dto.OrderDto
import dev.mo.surfcart.orders.dto.toOrder
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest
) : OrdersRepository {

    override suspend fun getOrders(): List<Order> {
        return safeSupabaseCall {
            postgrest.rpc("get_customer_orders")
                .decodeList<OrderDto>()
                .map { it.toOrder() }
        }
    }
}