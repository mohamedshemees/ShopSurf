package dev.mo.surfcart.checkout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.cart.CartItem
import dev.mo.surfcart.core.adapter.MyDiffUtil
import dev.mo.surfcart.databinding.ItemCheckoutProductBinding

class CheckoutProductsAdapter() :
    RecyclerView.Adapter<CheckoutProductsAdapter.CheckoutProductsViewHolder>() {
    private var products = emptyList<CartItem>()
    fun submitList(newProducts: List<CartItem>) {
        val diffUtil = MyDiffUtil(
            oldList = products,
            newList = newProducts,
            areItemsTheSame = { old, new -> old.productId == new.productId },
            areContentsTheSame = { old, new -> old == new })
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        products = newProducts
        diffResults.dispatchUpdatesTo(this)
    }

    class CheckoutProductsViewHolder(itemView: ItemCheckoutProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(productItem: CartItem) {
            binding.productName.text = productItem.title
            binding.productQuantity.text = productItem.quantity.toString()
            binding.productPrice.text = productItem.price.times(productItem.quantity).toString()
            Glide.with(binding.root).load(productItem.imageUrl).into(binding.productThumbnail)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CheckoutProductsViewHolder {
        val binding =
            ItemCheckoutProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CheckoutProductsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CheckoutProductsViewHolder, position: Int
    ) {
        val item = products[position]
        holder.bind(item)
    }

    override fun getItemCount() = products.size
}