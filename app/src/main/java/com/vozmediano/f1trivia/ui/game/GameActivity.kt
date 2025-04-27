package com.vozmediano.f1trivia.ui.game

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vozmediano.f1trivia.R
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
        setContentView(binding.root)

        optionsTv = listOf(
            binding.tvOptionOne,
            binding.tvOptionTwo,
            binding.tvOptionThree,
            binding.tvOptionFour
        )
        binding.tvPointsValue.text = "0"

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
                    if (it.image != "") {
                        binding.cvImageContainer.isVisible = true
                        binding.cvImageContainer.isEnabled = true
                        Glide
                            .with(binding.ivQuestion.context)
                            .load("https:${it.image}")
                            .into(binding.ivQuestion)
                    } else {
                        binding.cvImageContainer.isVisible = false
                        binding.cvImageContainer.isEnabled = false
                    }
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
            val currentPointsText = binding.tvPointsValue.text.toString()
            if (currentPointsText.isNotEmpty()) {
                (currentPointsText.toInt() + 1).toString()
                    .also { binding.tvPointsValue.text = it }
            } else {
                binding.tvPointsValue.text = "1" // Handle the case where it's unexpectedly empty
            }
            lifecycleScope.launch {
                generateQuestion()
            }
        } else {
            val score = binding.tvPointsValue.text.toString().toIntOrNull()
                ?: 0 // Use toIntOrNull for safety
            //binding.tvPointsValue.text = "0"

            if (isUserLoggedIn()) saveScore(score)

            AlertDialog.Builder(this)
                .setTitle("Game over")
                .setMessage("Score: $score")
                .setPositiveButton("OK") { _, _ ->
                    onBackPressedDispatcher.onBackPressed()
                }
                .show()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        Log.i("GameActivity", "(isUserLoggedIn) Checking login status")
        val currentUser = FirebaseAuth.getInstance().currentUser
        Log.i("GameActivity", "(isUserLoggedIn) Current user: $currentUser")
        return currentUser != null
    }

    private fun saveScore(score: Int) {
        Log.i("GameActivity", "Saving score: $score")
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val scoreRef = FirebaseFirestore.getInstance().collection("scores")
        val scoreMap = mapOf(
            "uid" to uid,
            "score" to score,
            "timestamp" to System.currentTimeMillis()
        )
        scoreRef.add(scoreMap)
            .addOnSuccessListener {
                Log.i("GameActivity", "Score saved successfully!")
            }
            .addOnFailureListener { e ->
                Log.e("GameActivity", "Error saving score: ${e.message}")
            }
    }

    private fun resetButtons(buttons: List<TextView>) {
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundColor(getColor(R.color.white))
        }
    }
}
