package dev.mo.surfcart.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.databinding.ItemCheckoutProductBinding

class SearchAdapter(
    private val onProductClick: (productId: Long) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var products = listOf<Product>()

    inner class SearchViewHolder(
        private val binding: ItemCheckoutProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                productName.text = product.productName
                productName.text = product.productDescription
                productPrice.text = "$${product.rating}"

                Glide.with(root.context)
                    .load(product.productThumbnail)
                    .centerCrop()
                    .into(productThumbnail)

                root.setOnClickListener { onProductClick(product.productId) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemCheckoutProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size

    fun submitList(newList: List<Product>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = products.size
            override fun getNewListSize() = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                products[oldItemPosition].productId == newList[newItemPosition].productId
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                products[oldItemPosition] == newList[newItemPosition]
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        products = newList
        diffResult.dispatchUpdatesTo(this)
    }
}
