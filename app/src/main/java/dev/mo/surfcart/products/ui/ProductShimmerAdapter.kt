package dev.mo.surfcart.products.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.mo.surfcart.R
import dev.mo.surfcart.core.adapter.MyDiffUtil
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.databinding.ItemProduct1Binding
import dev.mo.surfcart.databinding.ItemProduct1ShimmerBinding
import kotlin.math.roundToInt

class ProductShimmerAdapter()  : RecyclerView.Adapter<ProductShimmerAdapter.ShimmerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProduct1ShimmerBinding.inflate(inflater, parent, false)
        return ShimmerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 10

    inner class ShimmerViewHolder(val binding: ItemProduct1ShimmerBinding) :
        RecyclerView.ViewHolder(binding.root)
}
