package dev.mo.surfcart.registration.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentOtpBottomSheetBinding
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OtpBottomSheetFragment(
    private val email:String,
    private val onVerifiedSuccessfully: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentOtpBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OtpBottomSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeUiState()


        binding.etOtp.requestFocus()
    }

    private fun setupListeners() {
        binding.etOtp.doAfterTextChanged {
            if (binding.etOtp.error != null) {
                binding.etOtp.error = null
            }

        }


        binding.btnVerifyOtp.setOnClickListener {
            val otpInput = binding.etOtp.text.toString().trim()
            viewModel.verifyOtp(email,otpInput)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUiForState(state)
                }
            }
        }
    }

    private fun updateUiForState(state: OtpSheetUiState) {

        binding.btnVerifyOtp.isEnabled = state !is OtpSheetUiState.Loading
        binding.etOtp.isEnabled = state !is OtpSheetUiState.Loading

        when (state) {
            OtpSheetUiState.Idle -> {
                binding.tilOtp.error = null
                binding.btnVerifyOtp.text = "VERIFY"
            }

            OtpSheetUiState.Loading -> {
                binding.tilOtp.error = null
            }

            OtpSheetUiState.OtpVerified -> {
                onVerifiedSuccessfully()
                dismissAllowingStateLoss()
            }

            is OtpSheetUiState.Error -> {
                binding.tilOtp.error = state.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "OtpBottomSheetFragment"
        fun newInstance(email:String,onVerifiedSuccessfully: () -> Unit): OtpBottomSheetFragment {
            return OtpBottomSheetFragment(email,onVerifiedSuccessfully)
        }
    }
}