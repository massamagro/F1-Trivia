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
            viewModel.fetchDrivers()

        } catch (e: Exception) {
            Log.i("Tests", "error fetching (mainActivity)")
            Log.i("Tests", e.toString())
        }
        lifecycleScope.launch {
            viewModel.drivers.collectLatest { drivers ->
                if (drivers != null) {
                    // Update UI with driver data
                    binding.textView1.text = "${drivers.size} drivers"
                    binding.textView2.text = "number 133 ${drivers[133].givenName} ${drivers[133].familyName}"
                    binding.textView3.text = "number 10 ${drivers[10].givenName} ${drivers[10].familyName}"
                    binding.textView4.text = "number 0 ${drivers[0].givenName} ${drivers[0].familyName}"
                } else {
                    // Handle null case (e.g., loading, error)
                    Log.i("Tests", "(UI) null response")
                }
            }
        }
    }
}