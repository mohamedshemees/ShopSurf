package dev.mo.surfcart.categories.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.databinding.ItemCategoryCategoryFragmentBinding

class CalouselCategoriesAdapter() : RecyclerView.Adapter<CalouselCategoriesAdapter.CarouselViewHolder>() {
    private var categories = listOf<Category>()
    fun submitList(newList: List<Category>) {
        this.categories = newList
        notifyDataSetChanged()
    }

    class CarouselViewHolder(var itemCategory: ItemCategoryCategoryFragmentBinding) :
        ViewHolder(itemCategory.root) {

        fun bind(cateogry: Category) {

            Glide.with(itemCategory.root)
                .load(cateogry.categoryThumbnail)
                .into(itemCategory.carouselImageView)

            itemCategory.categoryTitle.text = cateogry.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemCategoryCategoryFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val cateogry = categories[position]
        holder.bind(cateogry)
    }


    override fun getItemCount(): Int = categories.size


}
