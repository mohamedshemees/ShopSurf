package dev.mo.surfcart.product_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager // Added import
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.cart.ui.CartViewModel
import dev.mo.surfcart.databinding.FragmentProductDetailsBinding
import dev.mo.surfcart.products.ui.ProductAdapter // Added import
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding // Changed to private
    private lateinit var disreiptionContainer: LinearLayout // Changed to private, consider removing if not used elsewhere
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var addToCartBtn: Button // Changed to private

    private lateinit var similarProductsAdapter: ProductAdapter // Added adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        addToCartBtn = binding.addToCartButton
        // Removed disreiptionContainer = binding.categoryDetailsContainer from here
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProductDetailsFragmentArgs by navArgs()
        val productId = args.productId

        disreiptionContainer = binding.categoryDetailsContainer

        setupSimilarProductsRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            initViews(productId)
        }


        initListeners(productId)
        observeSimilarProducts()
    }

    private fun setupSimilarProductsRecyclerView() {
        similarProductsAdapter = ProductAdapter { clickedProductId ->

            val action =
                ProductDetailsFragmentDirections.actionGlobalProductDetailsFragment(clickedProductId)
            findNavController().navigate(action)
        }
        binding.similarProductsRecyclerView.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = similarProductsAdapter
        }
    }

    private fun observeSimilarProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            productDetailsViewModel.similarProducts.collect { products ->
                similarProductsAdapter.submitList(products)
            }
        }
    }

    private fun initListeners(productId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            productDetailsViewModel.product.collect { product ->
                binding.productTitle.text = product?.productName
                binding.productPrice.text = product?.productPrice.toString()
                Glide.with(requireContext())
                    .load(product?.productThumbnail)
                    .into(binding.productThumbnail)
                binding.productDescription.text = product?.productDescription
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {

            val detailsMap = productDetailsViewModel.getProductDetails(productId)
            disreiptionContainer.removeAllViews()
            detailsMap.forEach {
                val textView = TextView(requireContext())
                textView.text = "${it.key}: ${it.value}"
                disreiptionContainer.addView(textView)
            }
        }
        addToCartBtn.setOnClickListener {
            cartViewModel.addToCart(productId.toInt())
        }
    }

    private suspend fun initViews(productId: Long) {
        productDetailsViewModel.getProductDetails(productId)
        productDetailsViewModel.getProductById(productId)
        productDetailsViewModel.fetchSimilarProducts()

    }
}
