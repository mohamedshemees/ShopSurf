package dev.mo.surfcart.registration.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.R
import dev.mo.surfcart.databinding.FragmentRegisterationBinding
import dev.mo.surfcart.home.ui.HomeFragment

@AndroidEntryPoint
class RegisterationFragment : Fragment() {
    lateinit var binding:FragmentRegisterationBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegisterationBinding.inflate(inflater,container,false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheet = OtpBottomSheetFragment.newInstance {

        }
        // Use childFragmentManager instead of parentFragmentManager
        bottomSheet.show(childFragmentManager, "OtpBottomSheet")

    }



}