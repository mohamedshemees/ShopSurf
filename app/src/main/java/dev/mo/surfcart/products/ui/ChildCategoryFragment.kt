package dev.mo.surfcart.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.R
import dev.mo.surfcart.core.entity.Product
import dev.mo.surfcart.databinding.FragmentChildcategoryBinding
import dev.mo.surfcart.products.ui.ProductAdapter
import dev.mo.surfcart.products.ui.ProductsViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChildCategoryFragment : Fragment() {
    private lateinit var binding: FragmentChildcategoryBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    lateinit var allProducts: List<Product>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildcategoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ChildCategoryFragmentArgs by navArgs()
        val categoryId = args.categoryId
        productAdapter = ProductAdapter()

        binding.productRv.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = productAdapter
        }

        viewModel.parentCategory=categoryId
        viewModel.loadCategories(categoryId)
        viewModel.loadProducts(categoryId)


        // Handle chip selection
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedChip = group.findViewById<Chip>(checkedId)
            val category = selectedChip?.text?.toString() ?: "All"

        }
        binding.chipAll.isChecked = true

        lifecycleScope.launch {
            viewModel.categories.collect { categories ->
                binding.chipGroup.removeAllViews()
                categories.forEach { category ->
                    val chip = Chip(requireContext()).apply {
                        text = category.name
                        isCheckable = true
                        isClickable = true
                        isFocusable = true
                        chipStrokeColor = ContextCompat.getColorStateList(requireContext(), R.color.chip_stroke)
                        chipStrokeWidth = 1f
                        tag = category.id.toLong()
                    }

                    binding.chipGroup.addView(chip)
                }
            }
        }

        binding.chipGroup.isSingleSelection = true

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collect { products ->
                productAdapter.submitList(products)
                allProducts = products
                Log.d("wow",products.toString())
            }
        }
    }
    private fun onChipSelected(category: Long): List<Product> {
        Log.d("wow", "Asdasd")
        val filteredProducts = if (category == -1L) {
            allProducts
        } else {
            allProducts.filter { it.categoryId == category }
        }
        Log.d("wow", filteredProducts.toString())
        return filteredProducts
    }

}
