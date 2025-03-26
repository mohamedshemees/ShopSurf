package dev.mo.surfcart.categories.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentCategoriesBinding
import dev.mo.surfcart.home.ui.CategoryAdapter
import dev.mo.surfcart.products.ui.ProductAdapter
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
                    carouselAdapter.submitList(categories)
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.subCategories.collect { subCategories ->
                subCategoriesAdapter.submitList(subCategories)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            categoriesViewModel.products.collect { products ->
                productAdapter.submitList(products)
            }
        }
    }

    private fun initViews() {
        carouselRecyclerView = binding.carouselRecyclerView
        carouselRecyclerView.layoutManager = CarouselLayoutManager()

        carouselAdapter = CarouselCategoriesAdapter {
            categoriesViewModel.loadSubCategories(it.toLong())
        }
        carouselRecyclerView.adapter = carouselAdapter
        carouselAdapter.snapHelperCallback.attachToRecyclerView(carouselRecyclerView)

        supCategoriesRecyclerView=binding.categoryRv

        supCategoriesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        subCategoriesAdapter = CategoryAdapter{
            categoriesViewModel.loadProducts(it)
        }

        binding.categoryRv.adapter = subCategoriesAdapter

        productAdapter = ProductAdapter()
        binding.productsRv.adapter = productAdapter
        binding.productsRv.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
    }

}