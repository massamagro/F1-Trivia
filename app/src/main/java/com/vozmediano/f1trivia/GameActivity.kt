package com.vozmediano.f1trivia

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.vozmediano.f1trivia.databinding.ActivityGameBinding
import com.vozmediano.f1trivia.domain.model.quiz.Option
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var optionsTv: List<TextView>

    private var currentOptions: List<Option> = emptyList()
    private val viewModel: GameViewModel by viewModels { GameViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        optionsTv = listOf(
            binding.tvOptionOne,
            binding.tvOptionTwo,
            binding.tvOptionThree,
            binding.tvOptionFour
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
                        .with(binding.ivQuestion.context)
                        .load("https:${it.image}")
                        .into(binding.ivQuestion)
                    binding.tvQuestion.text = it.title
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
                binding.tvQuestion.text = option.longText
            }
            optionsTv[index].isEnabled = false
        }


        if (isCorrect) {
            //binding.tvPointsValue.text = (binding.tvPointsValue.text.toString().toInt() + 1).toString()
            (binding.tvPointsValue.text.toString().toInt() + 1).toString()
                .also { binding.tvPointsValue.text = it }
            lifecycleScope.launch {
                generateQuestion()
            }
        } else {
            val score = binding.tvPointsValue.text.toString().toInt()
            binding.tvPointsValue.text = "0"

            AlertDialog.Builder(this)
                .setTitle("Game over")
                .setMessage("Score: $score")
                .setPositiveButton("OK") { _, _ ->
                    onBackPressedDispatcher.onBackPressed()
                }
                .show()

        }


    }


    private fun resetButtons(buttons: List<TextView>) {
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundColor(getColor(R.color.white))
        }
    }
}
