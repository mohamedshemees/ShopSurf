package dev.mo.surfcart.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.mo.surfcart.databinding.ItemCategoryShimmerBinding

class CategoryShimmerAdapter : RecyclerView.Adapter<CategoryShimmerAdapter.CategoryShimmerViewHolder>() {

    inner class CategoryShimmerViewHolder(binding: ItemCategoryShimmerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryShimmerViewHolder {
        val binding = ItemCategoryShimmerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryShimmerViewHolder(binding)
    }

    override fun getItemCount(): Int = 10 // A fixed number for shimmer

    override fun onBindViewHolder(holder: CategoryShimmerViewHolder, position: Int) {
        // No data to bind
    }
}
