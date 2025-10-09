package dev.mo.surfcart.checkout.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.R
import dev.mo.surfcart.databinding.FragmentCheckoutBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint

class CheckoutFragment : Fragment() {

    private lateinit var binding: FragmentCheckoutBinding
    private val viewModel: CheckoutViewModel by viewModels()

    private lateinit var checkoutProductsAdapter: CheckoutProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        checkoutProductsAdapter = CheckoutProductsAdapter()
        binding.orderItemsRv.apply {
            adapter = checkoutProductsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.placeOrderButton.setOnClickListener {
            viewModel.onPlaceOrderCLicked()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                if (state.isLoading) {
                    binding.progressBar.visibility =
                        View.VISIBLE
                    binding.orderSummaryCard.visibility= View.GONE
                    binding.orderItemsTv.visibility= View.GONE
                    binding.deliveryAddressTv.visibility= View.GONE
                    binding.paymentMethodTv.visibility= View.GONE
                    binding.placeOrderButton.visibility= View.GONE

                } else {
                    binding.progressBar.visibility =
                        View.GONE
                    binding.orderSummaryCard.visibility= View.VISIBLE
                    binding.orderItemsTv.visibility= View.VISIBLE
                    binding.deliveryAddressTv.visibility= View.VISIBLE
                    binding.paymentMethodTv.visibility= View.VISIBLE
                    binding.placeOrderButton.visibility= View.VISIBLE

                    checkoutProductsAdapter.submitList(state.checkoutProducts)
                    var totalPrice=0.0
                    state.checkoutProducts.forEach {productItem->
                        totalPrice+=productItem.price.times(productItem.quantity)
                    }
                    binding.totalvalue.text=totalPrice.toString()
                    binding.deliveryAddressGroup.removeAllViews()
                    state.userAddresses.forEachIndexed { index, address ->
                        val rb = RadioButton(requireContext()).apply {
                            setOnClickListener{
                                viewModel.updateAddress(address.id)
                            }
                            id = View.generateViewId()
                            text = address.street + address.city + address.country
                            setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.location_icon,
                                0, 0, 0
                            )
                            layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT,

                                )
                        }
                        binding.deliveryAddressGroup.addView(rb)
                        if (index == 0) binding.deliveryAddressGroup.check(rb.id)
                    }

                    binding.paymentMethodGroup.removeAllViews()
                    state.paymentMethods.forEachIndexed { index, method ->
                        val rb = RadioButton(requireContext()).apply {
                            setOnClickListener{
                                viewModel.updatePaymentMethod(method.id)
                            }
                            id = View.generateViewId()
                            text = method.provider
                            setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.credit_card,
                                0, 0, 0
                            )
                            layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                        binding.paymentMethodGroup.addView(rb)
                        if (index == 0) binding.paymentMethodGroup.check(rb.id)
                    }

                    state.errorMessage?.let { message ->
                        Log.d("WOWTEST",message)
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
