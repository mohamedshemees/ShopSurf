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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.cart.ui.CartViewModel
import dev.mo.surfcart.databinding.FragmentProductDetailsBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    lateinit var binding: FragmentProductDetailsBinding
    lateinit var disreiptionContainer: LinearLayout
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val cartViewModel:CartViewModel by activityViewModels()
    lateinit var addToCartBtn: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        addToCartBtn = binding.addToCartButton
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProductDetailsFragmentArgs by navArgs()
        val productId = args.productId
        disreiptionContainer = binding.categoryDetailsContainer


        viewLifecycleOwner.lifecycleScope.launch {
            initViews(productId)
        }
        initListeners(productId)
    }

    private fun initListeners(productId: Long) {
        lifecycleScope.launch {
            productDetailsViewModel.product.collect { product ->
                binding.productTitle.text = product?.productName
                binding.productPrice.text = product?.productPrice.toString()
                Glide.with(requireContext())
                    .load(product?.productThumbnail)
                    .into(binding.productThumbnail)
            }
        }
        lifecycleScope.launch {
            productDetailsViewModel.getProductDetails(productId)
                .forEach {
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
    }
}