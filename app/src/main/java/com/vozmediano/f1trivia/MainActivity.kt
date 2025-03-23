package com.vozmediano.f1trivia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.vozmediano.f1trivia.databinding.ActivityMainBinding
import com.vozmediano.f1trivia.domain.model.Driver
import com.vozmediano.f1trivia.ui.MainViewModel

class MainActivity : ComponentActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        try{
            var driver = viewModel.fetchDriver("alonso")
        }
        catch (e: Exception) {
            Log.i("Tests", e.toString())
        }

        //binding.textView.text = driver.toString()

    }
}




