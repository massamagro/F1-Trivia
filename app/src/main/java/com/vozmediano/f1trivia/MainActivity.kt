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
import kotlinx.coroutines.delay
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

        //viewModel.fetchCircuits()
        //viewModel.fetchDrivers()
        //viewModel.fetchConstructors()

        /*pick 4 random from drivers*/
        var question: Question = Question(title = "Who won the Monaco GP in 2021?")


        lifecycleScope.launch {
            // List of parameters for fetching different drivers
            val fetchParams = listOf(
                Pair("monaco", "1"),  // Position 1, Circuit Monaco
                Pair("monaco", "2"), // Position 2, Circuit Monaco
                Pair("monaco", "3"), // Position 3, Circuit Monaco
                Pair("monaco", "4")  // Position 4, Circuit Monaco
            )
            var drivers = mutableListOf<Driver>()

            for (i in 0..3) {
                lifecycleScope.launch {
                    Log.i(
                        "Tests",
                        "Fetching driver for position ${fetchParams[i].first} and circuit ${fetchParams[i].second}"
                    )
                    viewModel.fetchDriverBySeasonAndCircuitAndPosition(
                        "2021",
                        fetchParams[i].first,
                        fetchParams[i].second
                    )
                    viewModel.driver.collectLatest { driver ->

                        // Assign driver to the respective option
                        if (driver != null) {
                            drivers.add(driver)
                            question.options?.add(
                                Option(
                                    shortText = driver.givenName,
                                    longText = driver.familyName,
                                    id = i,
                                    isCorrect = i == 0 // Assuming the first driver is the correct one
                                )
                            )
                        }

                        return@collectLatest


                    }


                }
                delay(1000) // Adding a delay to avoid overwhelming the API

            }
            binding.textView.text = question.title
            binding.textView1.text = question.options?.get(0)?.shortText
            binding.textView2.text = question.options?.get(1)?.shortText
            binding.textView3.text = question.options?.get(2)?.shortText
            binding.textView4.text = question.options?.get(3)?.shortText


        }
    }
}