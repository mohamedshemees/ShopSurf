package dev.mo.surfcart

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.mo.surfcart.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        handleWindowInsets()
        initViews()
    }

    private fun handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0) // We handle bottom padding via NavHost
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentContentMain) { v, insets ->
            v.setPadding(0, 0, 0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom)
            insets
        }
    }

    private fun initViews() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}
