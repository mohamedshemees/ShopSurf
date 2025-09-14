package dev.mo.surfcart.registration.ui

import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    lateinit var binding: FragmentRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val args: RegistrationFragmentArgs by navArgs()
        val userType = args.type
        viewModel.setUserType(userType)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("WOW", "onViewCreated: ")
        initViews()
        collectUiState()
    }

    private fun initViews() {
        Log.d("WOW", "initViews: ")
        binding.apply {
            Log.d("WOW", "apply: ")

            etFirstName.doAfterTextChanged {
                viewModel.onNameChanged(it.toString())
            }

            etPhoneNumber.doAfterTextChanged {
                viewModel.onPhoneChanged(it.toString())
            }

            etEmail.doAfterTextChanged {
                viewModel.onEmailChanged(it.toString())
            }

            etPassword.doAfterTextChanged {
                viewModel.onPasswordChanged(it.toString())
            }

            etConfirmPassword.doAfterTextChanged {
                viewModel.onConfirmPasswordChanged(it.toString())
            }

            Log.d("WOW", "initViews: ")
            btnCreateAccount.setOnClickListener {
                Log.d("WOW", "initViews: ")
                viewModel.onCreateAccountClicked()
            }
        }

    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is RegistrationUiState.Success -> {
                            val data = state.data
                            binding.tvSubtitle.setText("Join us as a ${data.userType}")
                            Log.d("WOW", "data: ${data}")
                            Log.d(
                                "WOW", "collectUiState: ${
                                    data.name.isNotBlank() && data.phone.isNotBlank() && data.email.isNotBlank() && data.password.isNotBlank() && data.confirmPassword.isNotBlank()

                                }"
                            )
                            binding.btnCreateAccount.isEnabled =
                                data.name.isNotBlank() && data.phone.isNotBlank() && data.email.isNotBlank() && data.password.isNotBlank() && data.confirmPassword.isNotBlank()
                        }

                        is RegistrationUiState.OtpSent -> {
                            Toast.makeText(requireContext(), "OTP Sent!", Toast.LENGTH_SHORT).show()
                        }

                        is RegistrationUiState.OtpVerified -> {
                            Toast.makeText(requireContext(), "OTP Verified!", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is RegistrationUiState.OtpResent -> {
                            Toast.makeText(requireContext(), "OTP Resent!", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is RegistrationUiState.Error -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
                            // viewModel.resetToForm()
                        }
//                        RegistrationUiState.Loading -> {
//                            // Show progress bar
//                        }
                    }
                }
            }
        }
    }
}