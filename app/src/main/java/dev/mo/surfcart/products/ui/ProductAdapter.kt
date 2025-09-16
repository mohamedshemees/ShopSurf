package dev.mo.surfcart.products.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.R
import dev.mo.surfcart.core.adapter.MyDiffUtil
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.databinding.ItemProduct1Binding
import kotlin.math.roundToInt

class ProductAdapter(val onCLick: (productId: Long) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    lateinit var productItemBinding: ItemProduct1Binding

    private var products = listOf<Product>()

    fun submitList(newProducts: List<Product>) {
        val diffUtil= MyDiffUtil(
            oldList = products,
            newList = newProducts,
            areItemsTheSame = { old, new -> old.productId == new.productId },
            areContentsTheSame = { old, new -> old == new }
        )
        val diffResults= DiffUtil.calculateDiff(diffUtil)
        products=newProducts
        diffResults.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        productItemBinding =
            ItemProduct1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(productItemBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
        holder.binding.root.setOnClickListener {
            onCLick(products[position].productId)
        }

    }

    override fun getItemCount() = products.size

    class ProductViewHolder(itemView: ItemProduct1Binding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(product: Product) {
            binding.productName.text = product.productName
            binding.price.apply {
                text = product.productPrice.toString()
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            binding.productDescription.text = product.productDescription
            binding.rating.text = product.rating.toString()
            binding.salePercentage.text = product.discountPrice?.let { discountPrice ->
                ((product.productPrice - discountPrice) / product.productPrice * 100).roundToInt()
            }.toString() + "%"
            binding.afterSale.text = product.discountPrice.toString()
            Glide.with(binding.root)
                .load(product.productThumbnail)
                .placeholder(R.drawable.heading)
                .into(binding.productThumbnail)
        }
    }
}

