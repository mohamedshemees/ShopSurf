package dev.mo.surfcart.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.core.adapter.MyDiffUtil // Import your MyDiffUtil
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.databinding.ItemCategoryBinding

class CategoryAdapter(
    var onClick: (Long) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categories = listOf<Category>()

    // Updated submitList to use MyDiffUtil
    fun submitList(newList: List<Category>) {
        val diffCallback = MyDiffUtil(
            oldList = categories,
            newList = newList,
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id }, // Assuming 'id' is the unique identifier
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem } // Default content check, or define specific fields
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categories = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class CategoryViewHolder(
        categoryItem: ItemCategoryBinding,
        var onClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(categoryItem.root) {
        private val binding = categoryItem
        fun bind(category: Category) {
            binding.category.text = category.name
            Glide.with(binding.root)
                .load(category.categoryThumbnail)
                .centerCrop()
                .into(binding.categoryImage)
            binding.root.setOnClickListener {
                onClick(category.id.toLong())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, onClick)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }
}