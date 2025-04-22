package dev.mo.surfcart.product_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentProductDetailsBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    lateinit var binding: FragmentProductDetailsBinding
    lateinit var disreiptionContainer: LinearLayout
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProductDetailsFragmentArgs by navArgs()
        val productId = args.productId
        disreiptionContainer=binding.categoryDetailsContainer

        lifecycleScope.launch {
            initViews(productId)
        }

        initListeners()
    }

    private fun initListeners() {

    }

    private suspend fun initViews(productId: Long) {

        productDetailsViewModel.getProductDetails(productId)
        productDetailsViewModel.productDetails.collect { productDetails ->
           productDetails.forEach {
                val textView = TextView(requireContext())
                textView.text = "${it.key}: ${it.value}"
                disreiptionContainer.addView(textView)
            }
           }
    }
}