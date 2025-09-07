package dev.mo.surfcart.checkout.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import dev.mo.surfcart.R
import dev.mo.surfcart.databinding.FragmentCategoriesBinding
import dev.mo.surfcart.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {
    lateinit var binding: FragmentCheckoutBinding

    companion object {
        fun newInstance() = CheckoutFragment()
    }

    private val viewModel: CheckoutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding
    }
}