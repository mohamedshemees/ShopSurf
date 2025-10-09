package dev.mo.surfcart.cart

import dev.mo.surfcart.core.safeSupabaseCall
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
) : CartRepository {
    override suspend fun addToCart(productId: Int) {
        safeSupabaseCall {
            postgrest.rpc(
                "add_product_to_cart",
                JsonObject(mapOf("product_id" to JsonPrimitive(productId)))
            )
        }
    }

    override suspend fun increaseQuantity(productId: Int) {
        safeSupabaseCall {
            postgrest.rpc(
                "increase_quantity",
                JsonObject(mapOf("p_product_id" to JsonPrimitive(productId)))
            )
        }
    }

    override suspend fun decreaseQuantity(productId: Int) {
        safeSupabaseCall {
            postgrest.rpc(
                "decrease_quantity",
                JsonObject(mapOf("p_product_id" to JsonPrimitive(productId)))
            )
        }
    }

    override suspend fun getCartItems(): List<CartItem> {
        return safeSupabaseCall {
            postgrest.rpc(
                "get_cart_items",
            ).decodeList<CartItem>()
        }
    }

    override suspend fun clearCart(productId: Int) {
        safeSupabaseCall {
            postgrest.rpc("clear_cart")
        }
    }

    override suspend fun getTotalPrice(): Double {
        return safeSupabaseCall {
            postgrest.rpc("get_total_price").decodeSingle<Double>()
        }
    }

    override suspend fun removeProductFromCart(productId: Int) {
        safeSupabaseCall {
            postgrest.rpc(
                "remove_product_from_cart",
                JsonObject(mapOf("p_product_id" to JsonPrimitive(productId)))
            )
        }
    }
}