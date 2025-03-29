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

        viewModel.fetchConstructorsBySeason("2021")


        lifecycleScope.launch {
            viewModel.constructors.collectLatest { constructors ->
                if (constructors != null) {
                    // Update UI with driver data
                    binding.textView1.text = "${constructors.size} constructors"
                    binding.textView2.text =
                        "${constructors[0].name} - ${constructors[0].nationality}"
                    binding.textView3.text =
                        "${constructors[2].name} - ${constructors[2].nationality}"
                    binding.textView4.text =
                        "${constructors[6].name} - ${constructors[6].nationality}"
                } else {
                    // Handle null case (e.g., loading, error)
                    Log.i("Tests", "(UI) null response")
                }
            }
        }
    }
}