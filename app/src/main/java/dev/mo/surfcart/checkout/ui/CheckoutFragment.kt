package dev.mo.surfcart.checkout.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.R
import dev.mo.surfcart.core.UiEvent
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
        observeUiEvents()
    }

    private fun initViews() {
        checkoutProductsAdapter = CheckoutProductsAdapter()
        binding.orderItemsRv.apply {
            adapter = checkoutProductsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.placeOrderButton.setOnClickListener {
            viewModel.onPlaceOrderClicked()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                val contentVisibility = if (state.isLoading) View.GONE else View.VISIBLE
                binding.orderSummaryCard.visibility = contentVisibility
                binding.orderItemsTv.visibility = contentVisibility
                binding.deliveryAddressTv.visibility = contentVisibility
                binding.paymentMethodTv.visibility = contentVisibility
                binding.placeOrderButton.visibility = contentVisibility

                checkoutProductsAdapter.submitList(state.checkoutProducts)

                var totalPrice = 0.0
                state.checkoutProducts.forEach { productItem ->
                    totalPrice += productItem.price * productItem.quantity
                }
                binding.totalvalue.text = totalPrice.toString()

                if (binding.deliveryAddressGroup.childCount == 0 && state.userAddresses.isNotEmpty()) {
                    state.userAddresses.forEachIndexed { index, address ->
                        val rb = RadioButton(requireContext()).apply {
                            setOnClickListener { viewModel.updateAddress(address.id) }
                            id = View.generateViewId()
                            text = "${address.street}, ${address.city}, ${address.country}"
                            setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.location_icon,
                                0,
                                0,
                                0
                            )
                            layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                        binding.deliveryAddressGroup.addView(rb)
                        if (index == 0) {
                            binding.deliveryAddressGroup.check(rb.id)
                            viewModel.updateAddress(address.id)
                        }
                    }
                }

                if (binding.paymentMethodGroup.childCount == 0 && state.paymentMethods.isNotEmpty()) {
                    state.paymentMethods.forEachIndexed { index, method ->
                        val rb = RadioButton(requireContext()).apply {
                            setOnClickListener { viewModel.updatePaymentMethod(method.id) }
                            id = View.generateViewId()
                            text = method.provider
                            setCompoundDrawablesWithIntrinsicBounds(R.drawable.credit_card, 0, 0, 0)
                            layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            )
                        }
                        binding.paymentMethodGroup.addView(rb)
                        if (index == 0) {
                            binding.paymentMethodGroup.check(rb.id)
                            viewModel.updatePaymentMethod(method.id)
                        }
                    }
                }
            }
        }
    }

    private fun observeUiEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSuccessSnackbar -> {
                            showTopSnackbar(event.message, isError = false)
                            findNavController().navigate(CheckoutFragmentDirections.actionGlobalHomeScreen())
                        }

                        is UiEvent.ShowErrorSnackbar -> showTopSnackbar(
                            event.message,
                            isError = true
                        )
                    }
                }
            }
        }
    }

    private fun showTopSnackbar(message: String, isError: Boolean) {
        val snackbar = Snackbar.make(
            requireActivity().findViewById(R.id.main_activity),
            message,
            Snackbar.LENGTH_SHORT
        )

        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams

        params.gravity = Gravity.TOP

        val topInset = ViewCompat.getRootWindowInsets(requireActivity().window.decorView)
            ?.getInsets(WindowInsetsCompat.Type.statusBars())
            ?.top ?: 0
        params.topMargin = topInset

        view.layoutParams = params

        // Style it
        val color = ContextCompat.getColor(
            requireContext(),
            if (isError) R.color.md_theme_error else R.color.md_theme_success
        )
        snackbar.setBackgroundTint(color)
        snackbar.setTextColor(Color.WHITE)

        snackbar.show()
    }
}
