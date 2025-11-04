package dev.mo.surfcart.categories.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentCategoriesBinding
import dev.mo.surfcart.home.ui.CategoryAdapter
import dev.mo.surfcart.home.ui.CategoryShimmerAdapter
import dev.mo.surfcart.product_details.ui.ProductDetailsFragmentDirections
import dev.mo.surfcart.products.ui.ChildCategoryFragmentArgs
import dev.mo.surfcart.products.ui.ProductAdapter
import dev.mo.surfcart.products.ui.ProductShimmerAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var carouselRecyclerView: RecyclerView
    private lateinit var supCategoriesRecyclerView: RecyclerView
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var carouselAdapter: CarouselCategoriesAdapter
    private lateinit var snapHelper: CarouselSnapHelper
    private lateinit var subCategoriesAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productShimmerAdapter: ProductShimmerAdapter
    private lateinit var categoryShimmerAdapter: CategoryShimmerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ChildCategoryFragmentArgs by navArgs()
        val categoryId = args.categoryId

        initViews()


        binding.carouselShimmerLayout.startShimmer()
        binding.categoryShimmerLayout.startShimmer()
        binding.productShimmerLayout.startShimmer()
        
        categoriesViewModel.loadInitial(categoryId)

        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.parentCategories.collect { categories ->
                if (categories.isNotEmpty()) {
                    binding.carouselShimmerLayout.stopShimmer()
                    binding.carouselShimmerLayout.visibility = View.GONE
                    binding.carouselRecyclerView.visibility = View.VISIBLE
                }
                carouselAdapter.submitList(categories)
                val position = categories.indexOfFirst { it.id.toLong() == categoryId }
                if (position != -1) {
                    carouselRecyclerView.scrollToPosition(position)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.subCategories.collect { subCategories ->
                if (subCategories.isNotEmpty()){
                    binding.categoryShimmerLayout.stopShimmer()
                    binding.categoryShimmerLayout.visibility = View.GONE
                    binding.categoryRv.visibility = View.VISIBLE
                }
                subCategoriesAdapter.submitList(subCategories)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.products.collect { products ->
                if(products.isNotEmpty()){
                    binding.productShimmerLayout.stopShimmer()
                    binding.productShimmerLayout.visibility = View.GONE
                    binding.productsRv.visibility = View.VISIBLE
                }
                productAdapter.submitList(products)
            }
        }
    }

    private fun initViews() {
        carouselRecyclerView = binding.carouselRecyclerView
        carouselRecyclerView.layoutManager = CarouselLayoutManager()

        carouselAdapter = CarouselCategoriesAdapter {
            viewLifecycleOwner.lifecycleScope.launch {
                categoriesViewModel.loadSubCategories(it.toLong())
            }
        }

        carouselRecyclerView.adapter = carouselAdapter
        carouselAdapter.snapHelperCallback.attachToRecyclerView(carouselRecyclerView)

        supCategoriesRecyclerView = binding.categoryRv

        supCategoriesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        subCategoriesAdapter = CategoryAdapter {
            categoriesViewModel.loadProducts(it)
        }

        binding.categoryRv.adapter = subCategoriesAdapter

        productAdapter = ProductAdapter {
            val action = ProductDetailsFragmentDirections.actionGlobalProductDetailsFragment(it)
            findNavController().navigate(action)
        }

        binding.productsRv.adapter = productAdapter
        binding.productsRv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

        // Shimmer Adapters
        productShimmerAdapter = ProductShimmerAdapter()
        binding.productShimmerRv.adapter = productShimmerAdapter
        binding.productShimmerRv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)


        categoryShimmerAdapter = CategoryShimmerAdapter()
        binding.categoryShimmerRv.adapter = categoryShimmerAdapter
        binding.categoryShimmerRv.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)

    }

}