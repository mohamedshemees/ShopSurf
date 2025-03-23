package dev.mo.surfcart.categories.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentCategoriesBinding
import dev.mo.surfcart.products.ui.ProductAdapter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoriesFragment : Fragment() {


    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var carouselRecyclerView: RecyclerView
    private lateinit var supCategoriesRecyclerView: RecyclerView
    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var carouselAdapter: CalouselCategoriesAdapter
    private lateinit var snapHelper: CarouselSnapHelper
    private lateinit var subCategoriesAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentCategoriesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.parentCategories.collect { categories ->
                Log.d("wow", "Collecting parent categories: $categories")
                carouselAdapter.submitList(categories)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.subCategories.collect { subCategories ->
                Log.d("wow", "Collecting subcategories: $subCategories")
                subCategoriesAdapter.submitList(subCategories)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.products.collect { products ->
                Log.d("wow","Collecting products: $products")
                productAdapter.submitList(products)
            }
        }
    }

    private fun initViews() {
        carouselRecyclerView = binding.carouselRecyclerView
        carouselRecyclerView.layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy())
        snapHelper = CarouselSnapHelper()
        snapHelper.attachToRecyclerView(carouselRecyclerView)
        carouselAdapter = CalouselCategoriesAdapter()
        carouselRecyclerView.adapter = carouselAdapter

        binding.categoryRv.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        subCategoriesAdapter = CategoryAdapter{

        }
        binding.categoryRv.adapter = subCategoriesAdapter

        productAdapter = ProductAdapter()
        binding.productsRv.adapter = productAdapter
        binding.productsRv.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
    }

}