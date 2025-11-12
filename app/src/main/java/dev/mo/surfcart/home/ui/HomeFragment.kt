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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentHomeBinding
import dev.mo.surfcart.product_details.ui.ProductDetailsFragmentDirections
import dev.mo.surfcart.products.ui.ProductAdapter
import dev.mo.surfcart.products.ui.ProductShimmerAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var onSaleProductsAdapter: ProductAdapter
    private lateinit var onSaleProductsShimmerAdapter: ProductShimmerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryShimmerAdapter: CategoryShimmerAdapter
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchShimmerAdapter: SearchShimmerAdapter

    private var bannerHandler: Handler? = null
    private var bannerRunnable: Runnable? = null
    private var isUserSwiping = false

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
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    private fun initViews() {
        setupCategoriesRecyclerView()
        setupCategoriesShimmerRecyclerView()
        setupOnSaleProductsRecyclerView()
        setupOnSaleProductsShimmerRecyclerView()
        setupSearch()
    }

    private fun setupCategoriesRecyclerView() {
        categoryAdapter = CategoryAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToProductsFragment(it)
            findNavController().navigate(action)
        }
        binding.categoryRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun setupCategoriesShimmerRecyclerView() {
        categoryShimmerAdapter = CategoryShimmerAdapter()
        binding.categoryshimmerRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = categoryShimmerAdapter
        }
    }

    private fun setupOnSaleProductsRecyclerView() {
        onSaleProductsAdapter = ProductAdapter {
            val action = ProductDetailsFragmentDirections.actionGlobalProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.onSaleRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = onSaleProductsAdapter
        }
    }

    private fun setupOnSaleProductsShimmerRecyclerView() {
        onSaleProductsShimmerAdapter = ProductShimmerAdapter()
        binding.productShimmerRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            adapter = onSaleProductsShimmerAdapter
        }
    }

    private fun setupSearch() {
        searchAdapter = SearchAdapter {
            val action = ProductDetailsFragmentDirections.actionGlobalProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.searchResultsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }

        searchShimmerAdapter = SearchShimmerAdapter()
        binding.searchResultsShimmerRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchShimmerAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText?.trim().orEmpty()
                if (query.isNotEmpty()) {
                    viewModel.onSearchChanged(query)
                    binding.searchResultsShimmerLayout.startShimmer()
                    binding.searchResultsShimmerLayout.visibility = View.VISIBLE
                    binding.searchResultsRv.visibility = View.GONE
                } else {
                    binding.searchResultsShimmerLayout.stopShimmer()
                    binding.searchResultsShimmerLayout.visibility = View.GONE
                    binding.searchResultsRv.visibility = View.GONE
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean = true
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    initBanner()
                }
                launch {
                    viewModel.parentCategories.collect { categories ->
                        if (categories.isNotEmpty()) {
                            binding.categoryshimmerLayout.stopShimmer()
                            binding.categoryshimmerLayout.visibility = View.GONE
                            binding.categoryRv.visibility = View.VISIBLE
                        }
                        categoryAdapter.submitList(categories)
                    }
                }
                launch {
                    viewModel.onSaleProducts.collect { products ->
                        if (products.isNotEmpty() && !viewModel.isLoading.value) {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.visibility = View.GONE
                            binding.onSaleRv.visibility = View.VISIBLE
                        }
                        onSaleProductsAdapter.submitList(products)
                    }
                }
                launch {
                    viewModel.products.collect { products ->
                        binding.searchResultsShimmerLayout.stopShimmer()
                        binding.searchResultsShimmerLayout.visibility = View.GONE
                        if (products.isNotEmpty()) {
                            binding.searchResultsRv.visibility = View.VISIBLE
                        }else{
                            binding.searchResultsRv.visibility = View.GONE

                        }
                        searchAdapter.submitList(products)
                    }
                }
            }
        }
    }

    private suspend fun initBanner() {
        viewModel.banners.collect {
            setupBanners(it)
        }
    }

    private fun setupBanners(images: List<String>) {
        val viewPager = binding.bannerVp
        viewPager.adapter = SliderApapter(images, viewPager)

        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(viewPager)
            binding.bannerShimmer.visibility = View.GONE

            bannerHandler = Handler(Looper.getMainLooper())
            bannerRunnable = object : Runnable {
                override fun run() {
                    if (!isUserSwiping) {
                        val nextItem = (viewPager.currentItem + 1) % images.size
                        viewPager.setCurrentItem(nextItem, true)
                    }
                    bannerHandler?.postDelayed(this, 4000)
                }
            }

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
        bannerRunnable?.let { runnable ->
            bannerHandler?.removeCallbacks(runnable)
            bannerHandler?.postDelayed(runnable, 4000)
        }
    }

    private fun stopAutoScroll() {
        bannerRunnable?.let { runnable ->
            bannerHandler?.removeCallbacks(runnable)
        }
    }
}
