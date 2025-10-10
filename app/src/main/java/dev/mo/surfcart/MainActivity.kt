package dev.mo.surfcart

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        handleWindowInsets()
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is MainViewModel.MainUiState.Loading -> binding.progressbar.visibility = View.VISIBLE
                    is MainViewModel.MainUiState.ShowHome -> {
                        binding.progressbar.visibility = View.GONE
                        initViews(true)
                    }
                    is MainViewModel.MainUiState.ShowLogin -> {
                        binding.progressbar.visibility = View.GONE
                        initViews(false)
                    }
                }
            }
        }

    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

     private fun initViews(isLoggedIn:Boolean) {
         val navHostFragment = supportFragmentManager
             .findFragmentById(R.id.fragment_content_main) as NavHostFragment
         navController = navHostFragment.navController
         val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
         navGraph.setStartDestination(
             if (isLoggedIn) R.id.homeFragment
             else R.id.loginFragment
         )
        navController.graph = navGraph
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.registrationFragment,
                R.id.loginFragment,
                R.id.onboardingFragment,
                R.id.productsDetailsFragment,
                R.id.checkoutFragment-> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}
