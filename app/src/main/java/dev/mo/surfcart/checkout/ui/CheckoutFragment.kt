package dev.mo.surfcart.checkout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.mo.surfcart.databinding.FragmentCheckoutBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private val viewModel: CheckoutViewModel by viewModels()

    // Declare adapters as member variables
    private lateinit var deliveryAddressAdapter: DeliveryAddressAdapter
    private lateinit var checkoutProductsAdapter: CheckoutProductsAdapter
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initViews()
        observeViewModel()
    }

    private fun initAdapters() {
        deliveryAddressAdapter = DeliveryAddressAdapter()
        checkoutProductsAdapter = CheckoutProductsAdapter()
        paymentMethodAdapter = PaymentMethodAdapter()
    }

    private fun initViews() {

        binding.orderItemsRv.adapter = checkoutProductsAdapter
        binding.orderItemsRv.layoutManager = LinearLayoutManager(requireContext())

        binding.deliveryAddressRv.adapter = deliveryAddressAdapter
        binding.deliveryAddressRv.layoutManager = LinearLayoutManager(requireContext())

        binding.paymentMethodRv.adapter = paymentMethodAdapter
        binding.paymentMethodRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                deliveryAddressAdapter.submitList(state.userAddresses)

                checkoutProductsAdapter.submitList(state.checkoutProducts)

                paymentMethodAdapter.submitList(state.paymentMethods)

                state.errorMessage?.let { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
