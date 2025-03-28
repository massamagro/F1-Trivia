package com.vozmediano.f1trivia

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.vozmediano.f1trivia.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        try{
            viewModel.fetchDriver("alonso")


        } catch (e: Exception) {
            Log.i("Tests", "error fetching (mainActivity)")
            Log.i("Tests", e.toString())
        }
        lifecycleScope.launch {
            viewModel.driver.collectLatest { driver ->
                if (driver != null) {
                    // Update UI with driver data
                    binding.textView.text = "${driver.givenName} ${driver.familyName}"
                    Log.i("Tests", "(UI) Driver received: $driver")
                } else {
                    // Handle null case (e.g., loading, error)
                    Log.i("Tests", "(UI) Driver is null")
                }
            }
        }


        //binding.textView.text = viewModel.fetchDriver("alonso")

    }
}