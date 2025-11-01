package dev.mo.surfcart.order_tracking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.mo.surfcart.databinding.FragmentOrderTrackingBinding

class OrderTrackingFragment : Fragment() {

    private var _binding: FragmentOrderTrackingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Implement logic to get order details and update the UI
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}