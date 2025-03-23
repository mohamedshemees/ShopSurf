package dev.mo.surfcart.categories.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.databinding.ItemCategoryBinding

class CategoryAdapter(
                      private var onClick: (Category) -> Unit
    ) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var categories = listOf<Category>()
    fun submitList(newSubCategories: List<Category>) {
        categories=newSubCategories
        notifyDataSetChanged()
    }

    class CategoryViewHolder(categoryItem: ItemCategoryBinding,var onClick: (Category) -> Unit) : RecyclerView.ViewHolder(categoryItem.root) {
        private val binding=categoryItem
        fun bind(category: Category){
            binding.category.text = category.name
            Glide.with(binding.root)
                .load(category.categoryThumbnail)
                .centerCrop()
                .into(binding.categoryImage)
            binding.root.setOnClickListener {
                onClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding,onClick)
    }

    override fun getItemCount(): Int =categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category=categories[position]
        holder.bind(category)
    }


}
