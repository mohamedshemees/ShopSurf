package dev.mo.surfcart

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel: MainViewModel by viewModels()
    private var isGraphInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        handleWindowInsets()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        if (!isGraphInitialized) {
            lifecycleScope.launch {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is MainViewModel.MainUiState.ShowHome -> {
                            setGraph(true)
                        }
                        is MainViewModel.MainUiState.ShowLogin -> {
                            setGraph(false)
                        }
                        is MainViewModel.MainUiState.UpdateAppTheme -> {
                            AppCompatDelegate.setDefaultNightMode(
                                if (state.theme) AppCompatDelegate.MODE_NIGHT_YES
                                else AppCompatDelegate.MODE_NIGHT_NO
                            )
                        }
                        else -> {}
                    }
                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.registrationFragment,
                R.id.loginFragment,
                R.id.onboardingFragment,
                R.id.productsDetailsFragment,
                R.id.checkoutFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
    private fun setGraph(isLoggedIn: Boolean) {
        if (isGraphInitialized) return
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(
            if (isLoggedIn) R.id.homeFragment else R.id.loginFragment
        )
        navController.graph = navGraph
        isGraphInitialized = true
    }
    }