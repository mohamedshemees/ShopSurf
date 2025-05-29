package dev.mo.surfcart.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.databinding.ItemCartProductBinding

class CartItemsAdapter(
    private val onItemClick: (productId: Int) -> Unit,
    private val onItemRemove: (productId: Int) -> Unit,
    private val onItemIncrement: (productId: Int) -> Unit,
    private val onItemDecrement: (productId: Int) -> Unit
):RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>() {
    private val cartItems: List<CartItem> = mutableListOf()
    fun updateCartItems(newCartItems: List<CartItem>) {
        (cartItems as MutableList).clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemsViewHolder {
        val binding =
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemsViewHolder(binding)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(holder: CartItemsViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
        holder.binding.root.setOnClickListener {
            onItemClick(cartItem.productId)
        }
        holder.binding.btnDecrease.setOnClickListener {
            onItemDecrement(cartItem.productId)
        }
        holder.binding.btnIncrease.setOnClickListener {
            onItemIncrement(cartItem.productId)
        }
        holder.binding.btnRemove.setOnClickListener {
            onItemRemove(cartItem.productId)
        }

    }

    class CartItemsViewHolder(itemView: ItemCartProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(cartItem: CartItem) {
            binding.productName.text = cartItem.title
            binding.productDescription.text = cartItem.description
            binding.productPrice.text = cartItem.price.toString()
            binding.productQuantity.text = cartItem.quantity.toString()
            Glide.with(binding.root)
                .load(cartItem.imageUrl)
                .placeholder(dev.mo.surfcart.R.drawable.heading)
                .into(binding.productThumbnail)

        }
    }
}
