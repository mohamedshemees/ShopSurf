package dev.mo.surfcart.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentHomeBinding
import dev.mo.surfcart.products.ui.ProductAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    lateinit var onSaleProductsAdapter: ProductAdapter
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var categoriesRecyclerView: RecyclerView
    lateinit var onSaleproductsRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        lifecycleScope.launch {

            initBanner()

        }
        lifecycleScope.launch {
            viewModel.parentCategories.collect { categories ->
                categoryAdapter.submitList(categories)
                }
        }
        lifecycleScope.launch {
            viewModel.onSaleProducts.collect { categories ->
                onSaleProductsAdapter.submitList(categories)

            }
        }
    }

    fun initViews() {
        categoriesRecyclerView=binding.categoryRv
        categoryAdapter=CategoryAdapter {
            val action = HomeFragmentDirections
                .actionHomeFragmentToProductsFragment(it)
            findNavController().navigate(action)
        }
        categoriesRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            2, GridLayoutManager.HORIZONTAL, false
        )

        onSaleproductsRecyclerView=binding.onSaleRv
        onSaleproductsRecyclerView .layoutManager = GridLayoutManager(
            requireContext(),
            2, GridLayoutManager.HORIZONTAL, false
        )
        binding.categoryRv.adapter = categoryAdapter

        onSaleProductsAdapter = ProductAdapter()
        binding.onSaleRv.adapter = onSaleProductsAdapter



    }
    private suspend fun initBanner() {
        binding.progressBarSlider.visibility = View.VISIBLE
        viewModel.banners.collect {
            banners(it)
            binding.progressBarSlider.visibility = View.GONE
        }
    }

    private fun banners(image: List<String>) {
        binding.bannerVp.adapter = SliderApapter(image, binding.bannerVp)
        binding.bannerVp.clipToPadding = false
        binding.bannerVp.clipChildren = false
        binding.bannerVp.offscreenPageLimit = 3
        binding.bannerVp.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val composotePageTransformation = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.bannerVp.setPageTransformer(composotePageTransformation)

        if (image.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.bannerVp)
        }
    }
}

