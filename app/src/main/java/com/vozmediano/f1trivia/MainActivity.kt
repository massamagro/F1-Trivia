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

        viewModel.fetchCircuits()



        lifecycleScope.launch {
            viewModel.circuits.collectLatest { circuits ->
                if (circuits != null) {
                    // Update UI with driver data
                    binding.textView1.text = "${circuits[64].circuitName} \nbased at ${circuits[64].location.country}"
                    binding.textView2.text = "${circuits[44].circuitName} \nbased at ${circuits[44].location.country}"
                    binding.textView3.text = "${circuits[14].circuitName} \nbased at ${circuits[14].location.country}"
                    binding.textView4.text = "${circuits[33].circuitName} \nbased at ${circuits[33].location.country}"
                } else {
                    // Handle null case (e.g., loading, error)
                    Log.i("Tests", "(UI) null response")
                }
            }
        }
    }
}