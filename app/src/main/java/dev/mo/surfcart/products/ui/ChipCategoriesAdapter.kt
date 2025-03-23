package dev.mo.surfcart.products.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import dev.mo.surfcart.R
import dev.mo.surfcart.core.entity.Category
import dev.mo.surfcart.databinding.ItemChipBinding

class ChipCategoriesAdapter(
    var categories: List<Category>,
    var onSelect: (Long) -> Unit
) : RecyclerView.Adapter<ChipCategoriesAdapter.ChipViewHolder>() {

    var selectedPosition = -1 // Moved to adapter scope

    inner class ChipViewHolder(private val chipItem: ItemChipBinding) : ViewHolder(chipItem.root) {
        private val name = chipItem.categoryName
        private val icon = chipItem.categoryIcon
        fun bind(category: Category, position: Int) {
            name.text = category.name
            Glide.with(itemView).load(category.categoryThumbnail).into(icon)
            chipItem.root.setBackgroundResource(R.drawable.chip_bg_selector)
            chipItem.root.isSelected = position == selectedPosition

            chipItem.root.setOnClickListener {
                val previousPosition = selectedPosition
                if (position == selectedPosition) {
                    selectedPosition = -1
                    this@ChipCategoriesAdapter.notifyItemChanged(previousPosition)
                    onSelect(-1L)
                } else{
                    selectedPosition = position
                    this@ChipCategoriesAdapter.notifyItemChanged(previousPosition)
                    this@ChipCategoriesAdapter.notifyItemChanged(selectedPosition)
                    onSelect(category.id.toLong())
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChipViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, position)
    }

    override fun getItemViewType(position: Int): Int {
        return position // Forces each item to have a unique type and prevents unwanted flickers
    }

    class ChipDiffCallback(
        private val oldList: List<Category>,
        private val newList: List<Category>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    fun updateChips(newChips: List<Category>) {
        val diffCallback = ChipDiffCallback(categories, newChips)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categories = newChips
        diffResult.dispatchUpdatesTo(this)
    }
}