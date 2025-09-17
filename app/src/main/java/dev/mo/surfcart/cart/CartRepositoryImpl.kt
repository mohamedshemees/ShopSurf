package dev.mo.surfcart.cart

import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : CartRepository {
    override suspend fun addToCart(productId: Int) {
        withContext(Dispatchers.IO) {
            postgrest.rpc(
                "add_product_to_cart",
                JsonObject(mapOf("product_id" to JsonPrimitive(productId)))
            )
        }
    }

    override suspend fun increaseQuantity(productId: Int) {
        withContext(Dispatchers.IO) {
            postgrest.rpc(
                "increase_quantity",
                JsonObject(mapOf("p_product_id" to JsonPrimitive(productId)))
            )
        }
    }

    override suspend fun decreaseQuantity(productId: Int) {
        withContext(Dispatchers.IO) {
            postgrest.rpc(
                "decrease_quantity",
                JsonObject(mapOf("p_product_id" to JsonPrimitive(productId)))
            )
        }
    }

    override suspend fun getCartItems(): List<CartItem> {
        return withContext(Dispatchers.IO) {
            postgrest.rpc(
                "get_cart_items",
            )
        }.decodeList<CartItem>()

    }

    override suspend fun clearCart(productId: Int) {
    }

    override suspend fun getTotalPrice(): Double {
        TODO("Not yet implemented")
    }

    override suspend fun removeProductFromCart(productId: Int) {
        withContext(Dispatchers.IO) {
            postgrest.rpc(
                "remove_product_from_cart",
                JsonObject(mapOf("p_product_id" to JsonPrimitive(productId)))
            )

        }
    }
}