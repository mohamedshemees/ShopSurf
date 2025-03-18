package dev.mo.surfcart.home.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dev.mo.surfcart.databinding.SliderItemContinerBinding


class SliderApapter(private var slideritems:List<String>,
                    private val viewPager2: ViewPager2)
    : RecyclerView.Adapter<SliderApapter.SliderViewholder>(){
        private lateinit var context:Context
        private val runnable= Runnable {
            slideritems=slideritems
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewholder {
            context=parent.context
            val binding = SliderItemContinerBinding.
            inflate(LayoutInflater.from(parent.context),parent,false)

            return SliderViewholder(binding)
        }

    override fun onBindViewHolder(holder: SliderViewholder, position: Int) {
        holder.setImage(slideritems[position],context)
        if (position==slideritems.lastIndex-1){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int =slideritems.size

    class SliderViewholder (private val binding: SliderItemContinerBinding):
        RecyclerView.ViewHolder(binding.root){

        fun setImage(sliderItems:String,context: Context){
            Glide.with(context)
                .load(sliderItems)
                .apply { RequestOptions.centerInsideTransform() }
                .into(binding.imagSlide)

        }

    }

}

