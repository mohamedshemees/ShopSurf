package dev.mo.surfcart.checkout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.mo.surfcart.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {
    lateinit var binding: FragmentCheckoutBinding
    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.orderItemsRv.adapter = CheckoutProductsAdapter()
        binding.deliveryAddressRv.adapter = DeliveryAddressAdapter()
        binding.paymentMethodRv.adapter = PaymentMethodAdapter()

        binding.orderItemsRv.layoutManager= LinearLayoutManager(requireContext())
        binding.deliveryAddressRv.layoutManager= LinearLayoutManager(requireContext())
        binding.paymentMethodRv.layoutManager= LinearLayoutManager(requireContext())
    }
}