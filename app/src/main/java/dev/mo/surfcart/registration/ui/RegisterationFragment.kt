package dev.mo.surfcart.registration.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentRegisterationBinding

@AndroidEntryPoint
class RegisterationFragment : Fragment() {
    lateinit var binding: FragmentRegisterationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterationBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = OtpBottomSheetFragment.newInstance {

        }
        bottomSheet.show(childFragmentManager, "OtpBottomSheet")

    }
}