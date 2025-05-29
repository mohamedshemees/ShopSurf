package dev.mo.surfcart.cart.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.cart.CartItemsAdapter
import dev.mo.surfcart.databinding.FragmentCartBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    lateinit var binding: FragmentCartBinding
    private val cartViewModel:CartViewModel by activityViewModels()
    private lateinit var cartItemsAdapter: CartItemsAdapter
    private lateinit var cartItemRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        cartItemRecyclerView=binding.cartRecyclerView
        cartItemRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        cartItemsAdapter = CartItemsAdapter(
            onItemClick = { openProductDetails(it) },
            onItemRemove =  cartViewModel::removeFromCart ,
            onItemIncrement =  cartViewModel::increaseQuantity ,
            onItemDecrement =  cartViewModel::decreaseQuantity
        )
        cartItemRecyclerView.adapter = cartItemsAdapter
        return binding.root
    }

    private fun openProductDetails(productId: Int) {
            val action = CartFragmentDirections.actionGlobalProductDetailsFragment(productId.toLong())
            findNavController().navigate(action)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            cartViewModel.cartItems.collect{
                cartItemsAdapter.updateCartItems(it)
            }
        }
    }
}