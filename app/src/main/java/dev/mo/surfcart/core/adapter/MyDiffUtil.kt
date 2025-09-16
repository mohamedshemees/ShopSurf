package dev.mo.surfcart.core.adapter
import androidx.recyclerview.widget.DiffUtil
class MyDiffUtil <T>(
    val oldList: List<T>,
    val newList: List<T>,
    private val areItemsTheSame: (T, T) -> Boolean,
    private val areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame( oldList[oldItemPosition],newList[newItemPosition] )
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame ( oldList[oldItemPosition],newList[newItemPosition] )
    }
}
