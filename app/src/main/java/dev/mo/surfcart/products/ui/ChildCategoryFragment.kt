package dev.mo.surfcart.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.databinding.FragmentChildcategoryBinding
import dev.mo.surfcart.products.ui.ChipCategoriesAdapter
import dev.mo.surfcart.products.ui.ProductAdapter
import dev.mo.surfcart.products.ui.ProductsViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChildCategoryFragment : Fragment() {
    private lateinit var binding: FragmentChildcategoryBinding
    private val viewModel : ProductsViewModel  by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var subCategoriesAdapter: ChipCategoriesAdapter
    private var allProducts = listOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildcategoryBinding.inflate(inflater, container, false)

        val args: ChildCategoryFragmentArgs by navArgs()
        val categoryId = args.categoryId
        viewModel.parentCategory = categoryId
        productAdapter = ProductAdapter()
        subCategoriesAdapter = ChipCategoriesAdapter(listOf()) {
            viewModel.selectCategory(it) // Load products when a chip is clicked
        }
        viewModel.loadCategories(categoryId)

        binding.productRv.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3, LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
        binding.subCategoriesRv.adapter = subCategoriesAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.categories.collect { categories ->
                    subCategoriesAdapter.updateChips(categories)

                }
            }
            launch {
                viewModel.products.collect { products ->
                    productAdapter.submitList(products)
                    allProducts = products
                    Log.d("wow", products.toString())
                }
            }
        }
        return binding.root
    }
}