package dev.mo.surfcart.registration.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentOtpBottomSheetBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtpBottomSheetFragment(private val onVerified: () -> Unit) : BottomSheetDialogFragment() {
    private var _binding: FragmentOtpBottomSheetBinding? = null
    val binding get() = _binding!!
    private val registrationViewModel: RegisterationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBottomSheetBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            observeViewModel()
            setupListeners()
        }
    }


    private fun setupListeners() {
        binding.sendotpBtn.setOnClickListener {
            val input=binding.email.text.toString().trim()
            viewLifecycleOwner.lifecycleScope.launch {
                 handleAction(input,registrationViewModel.uiState.value)
                }
            }
        }

    private suspend fun handleAction(input: String, state: RegistrationUiState) {
        if (input.isEmpty()){
            binding.emaillayout.error =when (state){
                RegistrationUiState.Default, is RegistrationUiState.Error ->"Email Cannot be empty"
                RegistrationUiState.OtpSent, RegistrationUiState.OtpResent->"Otp Cannot be empty"
                else-> null

            }
            return
        }

        when(state){
            RegistrationUiState.Default, is RegistrationUiState.Error ->{
                registrationViewModel.sendOtp(input)
                binding.email.text?.clear()
            }

            RegistrationUiState.OtpResent,RegistrationUiState.OtpSent ->{
                registrationViewModel.verifyOtp(input)
            }
            RegistrationUiState.OtpVerified ->{

            }
        }


    }
    private  fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            registrationViewModel.uiState.collectLatest{
                updateUiForState(it)
            }
        }
    }

    private fun configureInputAndButton(hint: String, buttonText: String, message: String) {
        binding.emaillayout.hint = hint
        binding.sendotpBtn.text = buttonText
        binding.messageText.text = message
    }

    private fun updateUiForState(state: RegistrationUiState) {
        binding.emaillayout.error = null
        when (state) {
            RegistrationUiState.Default -> {
                configureInputAndButton("Enter Email", "Send OTP", "")
            }

            RegistrationUiState.OtpSent -> {
                configureInputAndButton("Enter OTP", "Verify OTP", "OTP sent. Check your inbox!")
            }

            RegistrationUiState.OtpResent -> {
                configureInputAndButton("Enter OTP", "Verify OTP", "OTP resent successfully!")
            }

            RegistrationUiState.OtpVerified -> {
                binding.messageText.text = "Verification successful!"
                dismiss()
                onVerified()
            }

            is RegistrationUiState.Error -> {
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(onVerified: () -> Unit) = OtpBottomSheetFragment(onVerified)
    }
}

