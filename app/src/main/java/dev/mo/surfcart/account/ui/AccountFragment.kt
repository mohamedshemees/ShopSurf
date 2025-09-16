package dev.mo.surfcart.account.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.FragmentAccountBinding
@AndroidEntryPoint
class AccountFragment : Fragment() {
    private val viewModel: AccountViewModel by viewModels()
    lateinit var binding:FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener {
            viewModel.onLogoutClicked()
            val action=AccountFragmentDirections.actionAccountFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }
}
