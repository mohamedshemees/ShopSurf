package dev.mo.surfcart.checkout.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.mo.surfcart.account.data.dto.CustomerAddress
import dev.mo.surfcart.core.adapter.MyDiffUtil
import dev.mo.surfcart.databinding.ItemDeliveryAddressBinding

class DeliveryAddressAdapter() :
    RecyclerView.Adapter<DeliveryAddressAdapter.CheckoutAddressesViewHolder>() {
    private var addresses = emptyList<CustomerAddress>()
    fun submitList(newAddresses: List<CustomerAddress>) {
        val diffUtil = MyDiffUtil(
            oldList = addresses,
            newList = newAddresses,
            areItemsTheSame = { old, new -> old.street == new.street },
            areContentsTheSame = { old, new -> old == new })
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        addresses = newAddresses
        diffResults.dispatchUpdatesTo(this)
    }

    class CheckoutAddressesViewHolder(itemView: ItemDeliveryAddressBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(address: CustomerAddress) {
            binding.addressDetails.text = address.street + address.city + address.country


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckoutAddressesViewHolder {
        val binding =
            ItemDeliveryAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CheckoutAddressesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CheckoutAddressesViewHolder,
        position: Int
    ) {
        val item = addresses[position]
        holder.bind(item)
    }

    override fun getItemCount() = addresses.size
}