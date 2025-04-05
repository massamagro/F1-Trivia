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
import com.vozmediano.f1trivia.domain.model.f1.Driver
import com.vozmediano.f1trivia.domain.model.quiz.Option
import com.vozmediano.f1trivia.domain.model.quiz.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val question = Question(title = "Who won the Monaco GP in 2021?", options = mutableListOf())
        val fetchParams = listOf(
            Pair("monaco", "1"),
            Pair("monaco", "2"),
            Pair("monaco", "3"),
            Pair("monaco", "4")
        )
        val drivers = mutableListOf<Driver>()

        lifecycleScope.launch {
            for (i in 0..3) {
                Log.i(
                    "Tests",
                    "Fetching driver for position ${fetchParams[i].first} and circuit ${fetchParams[i].second}"
                )

                // Do the fetch on IO thread
                withContext(Dispatchers.IO) {
                    viewModel.fetchDriverBySeasonAndCircuitAndPosition(
                        "2021",
                        fetchParams[i].first,
                        fetchParams[i].second
                    )
                }

                // Wait for the driver result from the flow
                val driver = viewModel.driver.firstOrNull()
                if (driver != null) {
                    drivers.add(driver)
                    question.options?.add(
                        Option(
                            shortText = driver.givenName,
                            longText = driver.familyName,
                            id = i,
                            isCorrect = i == 0 // First one is correct
                        )
                    )
                }

                delay(5000) // Optional
            }

            // Now update UI
            binding.textView.text = question.title
            binding.textView1.text = question.options.getOrNull(0)?.shortText
            binding.textView2.text = question.options.getOrNull(1)?.shortText
            binding.textView3.text = question.options.getOrNull(2)?.shortText
            binding.textView4.text = question.options.getOrNull(3)?.shortText
        }
    }
}