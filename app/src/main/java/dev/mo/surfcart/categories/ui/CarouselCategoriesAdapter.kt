package dev.mo.surfcart.categories.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.databinding.ItemCategoryCategoryFragmentBinding


class CarouselCategoriesAdapter(
    private val onSnappedPosition: (Int) -> Unit  // Function to return category ID when snapped
) : ListAdapter<Category, CarouselCategoriesAdapter.CarouselViewHolder>(DiffUtile()) {

    private var currentSnappedPosition = 0

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = getItem(position).id.toLong()

    inner class CarouselViewHolder(
        private val binding: ItemCategoryCategoryFragmentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                Glide.with(root)
                    .load(category.categoryThumbnail)
                    .transition(DrawableTransitionOptions.withCrossFade(150))
                    .into(carouselImageView)

                categoryTitle.text = category.name
            }
        }
    }

    // SnapHelper callback to track exactly when item snaps to position
    val snapHelperCallback = object : PagerSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager,
            velocityX: Int,
            velocityY: Int
        ): Int {
            val snappedPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
            if (snappedPosition != RecyclerView.NO_POSITION && snappedPosition != currentSnappedPosition) {
                currentSnappedPosition = snappedPosition
                // Return the category ID of the snapped item
                onSnappedPosition(getItem(snappedPosition).id)
            }
            return snappedPosition
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
        holder.bind(getItem(position))
    }

    class DiffUtile : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}

//
//
//
//class CalouselCategoriesAdapter(
//    var onItemClick: (Int) -> Unit
//) : ListAdapter<Category, CalouselCategoriesAdapter.CarouselViewHolder>(DiffUtile()) {
//
//    class CarouselViewHolder(var itemCategory: ItemCategoryCategoryFragmentBinding) :
//        RecyclerView.ViewHolder(itemCategory.root) {
//
//        fun bind(cateogry: Category) {
//            Glide.with(itemCategory.root)
//                .load(cateogry.categoryThumbnail)
//                .into(itemCategory.carouselImageView)
//
//            itemCategory.categoryTitle.text = cateogry.name
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
//        val binding = ItemCategoryCategoryFragmentBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return CarouselViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
//        val cateogry = getItem(position)
//        holder.bind(cateogry)
//
//            onItemClick(cateogry.id)
//
//    }
//
//    class DiffUtile : DiffUtil.ItemCallback<Category>() {
//        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
//            return oldItem == newItem
//        }
//    }
//}