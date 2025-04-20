package com.vozmediano.f1trivia

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.vozmediano.f1trivia.databinding.ActivityMainBinding
import com.vozmediano.f1trivia.domain.model.quiz.Option
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var optionsTv: List<TextView>

    private var currentOptions: List<Option> = emptyList()
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

        optionsTv = listOf(
            binding.optionOneTv,
            binding.optionTwoTv,
            binding.optionThreeTv,
            binding.optionFourTv
        )

        setupClickListeners()
        observeQuestions()
        generateQuestion()

    }

    private fun setupClickListeners() {
        optionsTv.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                checkAnswer(index)
            }
        }
    }


    private fun observeQuestions() {
        lifecycleScope.launch {
            viewModel.question.collectLatest { question ->
                question?.let {
                    Log.d("MainActivity", "Question: ${it.title}")
                    Glide
                        .with(binding.questionIv.context)
                        .load("https:${it.image}")
                        .into(binding.questionIv)
                    binding.questionTv.text = it.title
                    currentOptions = it.options
                    resetButtons(optionsTv)
                    optionsTv.forEachIndexed { index, textView ->
                        textView.text = it.options[index].shortText
                    }
                }
            }
        }
    }

    private fun generateQuestion() {
        when ((1..4).random()) {
            1 -> viewModel.fetchQuestionDriverBySeasonAndCircuitAndPosition()
            2 -> viewModel.fetchQuestionDriverByNationality()
            3 -> viewModel.fetchQuestionDriverByWinsAtCircuit()
            4 -> viewModel.fetchQuestionDriverByPodiumsAtCircuit()
        }
    }

    //LISTENERS
    private fun checkAnswer(selectedIndex: Int) {
        val isCorrect = currentOptions[selectedIndex].isCorrect
        optionsTv[selectedIndex].setBackgroundColor(
            if (isCorrect) {
                getColor(R.color.green)
            } else {
                getColor(R.color.red)
            }
        )

        currentOptions.forEachIndexed { index, option ->
            if (option.isCorrect) {
                binding.questionTv.text = option.longText
            }
            optionsTv[index].isEnabled = false
        }


        if (isCorrect) {
            //binding.pointsValueTv.text = (binding.pointsValueTv.text.toString().toInt() + 1).toString()
            (binding.pointsValueTv.text.toString().toInt() + 1).toString()
                .also { binding.pointsValueTv.text = it }
        } else {
            binding.pointsValueTv.text = "0"
        }

        lifecycleScope.launch {
            generateQuestion()
        }
    }


    private fun resetButtons(buttons: List<TextView>) {
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundColor(getColor(R.color.white))
        }
    }
}
