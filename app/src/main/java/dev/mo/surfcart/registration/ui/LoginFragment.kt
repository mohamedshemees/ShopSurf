package dev.mo.surfcart.registration.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentLogInBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initCallBacks()
        collectUiState()
    }

    private fun initCallBacks() {
        binding.apply {
            btnLogin.setOnClickListener {
                viewModel.onLoginClicked()
            }

             tvForgetPassword.setOnClickListener {
                // findNavController().navigate(R.id.action_logInFragment_to_forgotPasswordFragment) // Example navigation
             }

             tvSignUp.setOnClickListener {
               navigateToBoarding() // Example navigation
             }
        }
    }

    private fun initViews() {
        binding.apply {
            etEmailLogin.doAfterTextChanged {
                viewModel.onEmailChanged(it.toString())
            }

            etPasswordLogin.doAfterTextChanged {
                viewModel.onPasswordChanged(it.toString())
            }

        }
    }
    private fun navigateToBoarding(){
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
        findNavController().navigate(action)
    }
    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { state ->
                    when (state) {
                        is LogInUiState.Success -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                          val action= LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                           findNavController().navigate(action)
                            binding.btnLogin.isEnabled = true
                        }
                        is LogInUiState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                            binding.btnLogin.isEnabled = true
                        }
                        is LogInUiState.Loading -> {
                            binding.btnLogin.isEnabled = false
                        }

                        is LogInUiState.FormInput -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}