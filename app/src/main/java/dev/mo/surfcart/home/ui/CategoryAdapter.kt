package dev.mo.surfcart.home.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.core.adapter.MyDiffUtil
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.databinding.ItemCategoryBinding

class CategoryAdapter(
    var onClick: (Long) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categories = listOf<Category>()
    private var currentCategory: Int = categories.firstOrNull()?.id ?: -1

    fun submitList(newList: List<Category>) {
        if (newList.isNotEmpty()) {
            if (currentCategory == -1 || newList.none { it.id == currentCategory }) {
                currentCategory = newList.first().id
            }
        }
        val diffCallback = MyDiffUtil(
            oldList = categories,
            newList = newList,
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categories = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CategoryViewHolder(
        categoryItem: ItemCategoryBinding,
    ) : RecyclerView.ViewHolder(categoryItem.root) {
        private val binding = categoryItem
        fun bind(category: Category) {
            binding.category.text = category.name
            Glide.with(binding.root)
                .load(category.categoryThumbnail)
                .centerCrop()
                .into(binding.categoryImage)
            if (category.id == currentCategory) {
                binding.gradientOverlay.visibility = View.VISIBLE
            } else {
                binding.gradientOverlay.visibility = View.GONE
            }
            binding.root.setOnClickListener {
                currentCategory = category.id
                onClick(category.id.toLong())
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }
}