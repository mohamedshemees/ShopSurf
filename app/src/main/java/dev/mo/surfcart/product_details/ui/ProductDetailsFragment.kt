package dev.mo.surfcart.product_details.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.R
import dev.mo.surfcart.cart.ui.CartFragment
import dev.mo.surfcart.cart.ui.CartViewModel
import dev.mo.surfcart.core.UiEvent
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.databinding.FragmentProductDetailsBinding
import dev.mo.surfcart.products.ui.ProductAdapter
import dev.mo.surfcart.products.ui.ProductShimmerAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()

    private lateinit var similarProductsAdapter: ProductAdapter
    private lateinit var similarProductsShimmerAdapter: ProductShimmerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        observeViewModel()
    }

    private fun initViews() {
        setupSimilarProductsRecyclerView()
        setupSimilarProductsShimmerRecyclerView()
    }

    private fun initListeners() {
        binding.addToCartButton.setOnClickListener {
            cartViewModel.addToCart(args.productId.toInt())
        }
        binding.cartFab.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }
    }

    private fun observeViewModel() {
        productDetailsViewModel.loadProductData(args.productId)
        observeUiState()
        observeUiEvents()
    }

    private fun setupSimilarProductsRecyclerView() {
        similarProductsAdapter = ProductAdapter { clickedProduct ->
            val action =
                ProductDetailsFragmentDirections.actionGlobalProductDetailsFragment(clickedProduct)
            findNavController().navigate(action)
        }
        binding.similarProductsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = similarProductsAdapter
        }
    }

    private fun setupSimilarProductsShimmerRecyclerView() {
        similarProductsShimmerAdapter = ProductShimmerAdapter()
        binding.shimmerRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = similarProductsShimmerAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productDetailsViewModel.uiState.collect { state ->
                    toggleShimmer(state.isLoading)
                    if (!state.isLoading) {
                        state.product?.let { product -> bindProductData(product) }
                        populateProductDetails(state.productDetails)
                        similarProductsAdapter.submitList(state.similarProducts)

                        state.errorMessage?.let { message ->
                            showTopSnackbar(message, isError = true)
                        }
                    }
                }
            }
        }
    }

    private fun observeUiEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.uiEvent.collect { event ->
                    when (event) {
                        is UiEvent.ShowSuccessSnackbar -> showTopSnackbar(event.message, isError = false)
                        is UiEvent.ShowErrorSnackbar -> showTopSnackbar(event.message, isError = true)
                    }
                }
            }
        }
    }

    private fun bindProductData(product: Product) {
        binding.productTitle.text = product.productName
        binding.productDescription.text = product.productDescription
        binding.productPrice.text = product.productPrice.toString()
        Glide.with(this)
            .load(product.productThumbnail)
            .placeholder(R.drawable.mosalah)
            .error(R.drawable.ic_cart_outlined)
            .into(binding.productThumbnail)
    }

    private fun populateProductDetails(details: Map<String, String>) {
        binding.categoryDetailsContainer.removeAllViews()
        val context = requireContext()
        details.forEach { (key, value) ->
            val keyText = "$key: "
            val fullText = keyText + value
            val spannable = SpannableString(fullText)

            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                keyText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val attributeTextView = TextView(context).apply {
                text = spannable
                textSize = 14f
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val marginBottom = (8 * resources.displayMetrics.density).toInt()
                params.bottomMargin = marginBottom
                layoutParams = params
            }
            binding.categoryDetailsContainer.addView(attributeTextView)
        }
    }

    private fun toggleShimmer(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                productDetailsShimmerLayout.startShimmer()
                similarProductsShimmerLayout.startShimmer()
                productDetailsShimmerLayout.visibility = View.VISIBLE
                similarProductsShimmerLayout.visibility = View.VISIBLE
                addToCartButton.visibility = View.GONE
                productThumbnail.visibility = View.GONE
            } else {
                productDetailsShimmerLayout.stopShimmer()
                similarProductsShimmerLayout.stopShimmer()
                productDetailsShimmerLayout.visibility = View.GONE
                similarProductsShimmerLayout.visibility = View.GONE
                addToCartButton.visibility = View.VISIBLE
                productThumbnail.visibility = View.VISIBLE
            }
        }
    }

    private fun showTopSnackbar(message: String, isError: Boolean) {
        val snackbar = Snackbar.make(requireActivity().findViewById(R.id.main_activity), message, Snackbar.LENGTH_SHORT)

        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams

        params.gravity = Gravity.TOP

        val topInset = ViewCompat.getRootWindowInsets(requireActivity().window.decorView)
            ?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
        params.topMargin = topInset

        view.layoutParams = params

        val color = ContextCompat.getColor(
            requireContext(),
            if (isError) R.color.md_theme_error else R.color.md_theme_success
        )
        snackbar.setBackgroundTint(color)
        snackbar.setTextColor(Color.WHITE)

        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.similarProductsRecyclerView.adapter = null
        binding.shimmerRecyclerView.adapter = null
        _binding = null
    }
}
