package dev.mo.surfcart.home.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.mo.surfcart.databinding.ItemSearchResultShimmerBinding

class SearchShimmerAdapter : RecyclerView.Adapter<SearchShimmerAdapter.SearchShimmerViewHolder>() {

    inner class SearchShimmerViewHolder(binding: ItemSearchResultShimmerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchShimmerViewHolder {
        val binding = ItemSearchResultShimmerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchShimmerViewHolder(binding)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: SearchShimmerViewHolder, position: Int) {

    }
}
