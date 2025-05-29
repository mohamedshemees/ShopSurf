package dev.mo.surfcart.cart

interface CartRepository {
    suspend fun addToCart(productId: Int)
    suspend fun increaseQuantity(productId: Int)
    suspend fun decreaseQuantity(productId: Int)
    suspend fun getCartItems(): List<CartItem>
    suspend fun clearCart(productId: Int)
    suspend fun getTotalPrice(): Double
    suspend fun removeProductFromCart(productId: Int)
}