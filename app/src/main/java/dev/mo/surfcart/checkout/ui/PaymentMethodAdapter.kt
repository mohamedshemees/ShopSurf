package dev.mo.surfcart.checkout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.mo.surfcart.core.adapter.MyDiffUtil // Assuming MyDiffUtil is in this package
import dev.mo.surfcart.databinding.ItemPaymentMethodBinding // Assuming this binding class exists

class PaymentMethodAdapter() :
    RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    private var paymentMethods = emptyList<PaymentMethodItem>()

    fun submitList(newPaymentMethods: List<PaymentMethodItem>) {
        val diffUtilCallback = MyDiffUtil(
            oldList = paymentMethods,
            newList = newPaymentMethods,
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        paymentMethods = newPaymentMethods
        diffResult.dispatchUpdatesTo(this)
    }

    class PaymentMethodViewHolder(itemView: ItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(paymentMethod: PaymentMethodItem) {
            binding.paymentType.text = paymentMethod.name
            binding.paymentDetails.text = paymentMethod.description ?: ""


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentMethodViewHolder {
        val binding =
            ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentMethodViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PaymentMethodViewHolder,
        position: Int
    ) {
        val item = paymentMethods[position]
        holder.bind(item)
    }

    override fun getItemCount() = paymentMethods.size
}
