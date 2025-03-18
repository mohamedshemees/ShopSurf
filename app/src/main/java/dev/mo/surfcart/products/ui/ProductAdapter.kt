package dev.mo.surfcart.products.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.databinding.ItemProduct1Binding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    lateinit var productItemBinding: ItemProduct1Binding

    private var tasks = listOf<Product>()

    fun submitList(newTasks: List<Product>) {
        tasks = newTasks
        notifyDataSetChanged() // Use DiffUtil in production for efficiency
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        productItemBinding =
            ItemProduct1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(productItemBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(tasks[position])

    }

    override fun getItemCount() = tasks.size

    class ProductViewHolder(itemView: ItemProduct1Binding) : RecyclerView.ViewHolder(itemView.root) {
        val binding=itemView

        fun bind(product: Product) {
            binding.productName.text = product.productName
            binding.price.text = product.productPrice.toString()
            binding.productDescription.text = product.productDescription
            binding.rating.text = product.rating.toString()
            binding.afterSale.text = product.discountPrice.toString()

            Glide.with(binding.root)
                .load(product.productThumbnail)
                .placeholder(dev.mo.surfcart.R.drawable.heading)
                .into(binding.productThumbnail)


        }
    }
}
