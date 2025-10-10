package dev.mo.surfcart.home.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentHomeBinding
import dev.mo.surfcart.product_details.ui.ProductDetailsFragmentDirections
import dev.mo.surfcart.products.ui.ProductAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    lateinit var onSaleProductsAdapter: ProductAdapter
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var searchAdapter: SearchAdapter

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
        lifecycleScope.launch {
            viewModel.products.collect { prodcuts ->
                searchAdapter.submitList(prodcuts)
            }
        }
    }

    private fun initViews() {
        categoriesRecyclerView = binding.categoryRv
        categoryAdapter = CategoryAdapter {
            val action = HomeFragmentDirections
                .actionHomeFragmentToProductsFragment(it)
            findNavController().navigate(action)
        }
        categoriesRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            2, GridLayoutManager.HORIZONTAL, false
        )

        onSaleproductsRecyclerView = binding.onSaleRv
        onSaleproductsRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            2, GridLayoutManager.HORIZONTAL, false
        )
        binding.categoryRv.adapter = categoryAdapter

        onSaleProductsAdapter = ProductAdapter {
            val action = ProductDetailsFragmentDirections.actionGlobalProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.onSaleRv.adapter = onSaleProductsAdapter

        searchAdapter = SearchAdapter {
            val action = ProductDetailsFragmentDirections.actionGlobalProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.searchResultsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.searchResultsRv.adapter = searchAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText?.trim().orEmpty()

                if (query.isNotEmpty()) {
                    viewModel.onSearchChanged(query)
                    binding.searchResultsRv.visibility = View.VISIBLE
                } else {
                    binding.searchResultsRv.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?) = true
        })
    }

    private suspend fun initBanner() {
        binding.progressBarSlider.visibility = View.VISIBLE
        viewModel.banners.collect {
            banners(it)
            binding.progressBarSlider.visibility = View.GONE
        }
    }

    private var bannerHandler: Handler? = null
    private var bannerRunnable: Runnable? = null
    private var isUserSwiping = false

    private fun banners(images: List<String>) {
        val viewPager = binding.bannerVp
        viewPager.adapter = SliderApapter(images, viewPager)

        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(viewPager)

            bannerHandler = Handler(Looper.getMainLooper())
            bannerRunnable = object : Runnable {
                override fun run() {
                    if (!isUserSwiping) {
                        val nextItem = (viewPager.currentItem + 1) % images.size
                        viewPager.setCurrentItem(nextItem, true)
                    }
                    bannerHandler?.postDelayed(this, 4000) // 4 sec for natural rhythm
                }
            }

            // Page scroll callback to detect manual swiping
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        ViewPager2.SCROLL_STATE_DRAGGING -> isUserSwiping = true
                        ViewPager2.SCROLL_STATE_IDLE -> isUserSwiping = false
                    }
                }
            })

            startAutoScroll()
        } else {
            binding.dotIndicator.visibility = View.GONE
        }
    }

    private fun startAutoScroll() {
        bannerHandler?.removeCallbacks(bannerRunnable!!)
        bannerHandler?.postDelayed(bannerRunnable!!, 4000)
    }

    private fun stopAutoScroll() {
        bannerHandler?.removeCallbacks(bannerRunnable!!)
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }
}