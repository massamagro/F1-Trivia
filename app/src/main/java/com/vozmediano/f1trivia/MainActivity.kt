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
import com.vozmediano.f1trivia.databinding.ActivityMainBinding
import com.vozmediano.f1trivia.domain.model.quiz.Option
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
        generateQuestion()

    }

    private fun generateQuestion(){

        val questionTv = binding.textView
        val optionOne = binding.optionOne
        val optionTwo = binding.optionTwo
        val optionThree = binding.optionThree
        val optionFour = binding.optionFour
        resetButtons(listOf(optionOne, optionTwo, optionThree, optionFour))
        viewModel.fetchQuestionDriverBySeasonAndCircuitAndPosition()

        lifecycleScope.launch {
            viewModel.question.collectLatest { question ->
                question?.let {
                    Log.d("MainActivity", "Question: ${it.title}")
                    questionTv.text = it.title
                    optionOne.text = "${it.options[0].shortText}"
                    optionTwo.text = "${it.options[1].shortText}"
                    optionThree.text = "${it.options[2].shortText}"
                    optionFour.text = "${it.options[3].shortText}"
                    var optionsTv = listOf<TextView>(optionOne, optionTwo, optionThree, optionFour)

                    optionOne.setOnClickListener {
                        checkAnswer(question.options, optionsTv)
                    }
                    optionTwo.setOnClickListener {
                        checkAnswer(question.options, optionsTv)
                    }
                    optionThree.setOnClickListener {
                        checkAnswer(question.options, optionsTv)
                    }
                    optionFour.setOnClickListener {
                        checkAnswer(question.options, optionsTv)
                    }

                }
            }

        }
    }

    //LISTENERS
    private fun checkAnswer(options: List<Option>, optionsTv: List<TextView>) {
        options.forEach() { option ->
            if (option.isCorrect) {
                optionsTv[option.id].setBackgroundColor(getColor(R.color.green))
                generateQuestion()
            } else {
                optionsTv[option.id].setBackgroundColor(getColor(R.color.red))
            }
            optionsTv[option.id].isEnabled = false
        }
        lifecycleScope.launch {
            delay(2000)
        }
    }

    private fun resetButtons(buttons: List<TextView>) {
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundColor(getColor(R.color.white))
        }
    }
}
