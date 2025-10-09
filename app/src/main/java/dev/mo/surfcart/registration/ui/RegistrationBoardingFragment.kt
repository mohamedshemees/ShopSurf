package dev.mo.surfcart.registration.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.mo.surfcart.databinding.FragmentRegistrationBoardingBinding

class RegistrationBoardingFragment : Fragment() {

    private var _binding: FragmentRegistrationBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

    }

    private fun setupClickListeners() {
        binding.cardSeller.setOnClickListener {
            navigateToRegistration("Seller")
        }

        binding.cardCustomer.setOnClickListener {
            navigateToRegistration("Customer")
        }
        binding.tvSignIn.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToRegistration(userType: String) {
        val action = RegistrationBoardingFragmentDirections.actionRegistrationBoardingFragmentToRegistrationFragment(userType)
        findNavController().navigate(action)
    }
    private fun navigateToLogin() {
        val action = RegistrationBoardingFragmentDirections.actionRegistrationBoardingFragmentToLoginFragment()
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}