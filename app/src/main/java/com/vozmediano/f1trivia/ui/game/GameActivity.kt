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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var optionsTv: List<TextView>
    private var currentOptions: List<Option> = emptyList()
    private val viewModel: GameViewModel by viewModels { GameViewModel.Factory }
    private var lives = 3

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
        observeLoadingState()
        generateQuestion() // opening question
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
                    Log.d("GameActivity", "Question: ${it.title}")
                    loadImage(it.image)
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

    private fun observeLoadingState() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest { loading ->
                binding.progressBar.isVisible = loading
                binding.tvQuestion.isVisible = !loading
                binding.cvImageContainer.isVisible = !loading
                binding.glLives.isVisible = !loading
                binding.glOptions.isVisible = !loading
            }
        }
    }

    private fun loadImage(image: String?) {
        if (!image.isNullOrEmpty()) {
            binding.cvImageContainer.isVisible = true
            binding.cvImageContainer.isEnabled = true
            Glide
                .with(this)
                .load("https:${image}")
                .into(binding.ivQuestion)
        } else {
            binding.cvImageContainer.isVisible = false
            binding.cvImageContainer.isEnabled = false
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

    private fun checkAnswer(selectedIndex: Int) {
        optionsTv.forEach { it.isEnabled = false }
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
        }

        lifecycleScope.launch {
            delay(1000) //delay to show correct answer explanation
            if (isCorrect) {
                val currentPointsText = binding.tvPointsValue.text.toString()
                val newPoints = if (currentPointsText.isNotEmpty()) {
                    currentPointsText.toInt() + 1
                } else {
                    1
                }
                binding.tvPointsValue.text = newPoints.toString()
                generateQuestion()
            } else {
                when (lives) {
                    3 -> binding.ivLifeThree.setImageResource(R.drawable.black_heart_logo)
                    2 -> binding.ivLifeTwo.setImageResource(R.drawable.black_heart_logo)
                    1 -> binding.ivLifeOne.setImageResource(R.drawable.black_heart_logo)
                }
                lives--
                if (lives == 0) {
                    endGame()
                } else {
                    generateQuestion()
                }
            }
        }
    }

    private fun endGame() {
        val score = binding.tvPointsValue.text.toString().toIntOrNull() ?: 0
        if (isUserLoggedIn()) saveScore(score)
        AlertDialog.Builder(this)
            .setTitle("Game over")
            .setMessage("Score: $score")
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .show()
    }

    private fun isUserLoggedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    private fun saveScore(score: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val scoreRef = FirebaseFirestore.getInstance().collection("scores")
        val scoreMap = mapOf("uid" to uid, "score" to score, "timestamp" to System.currentTimeMillis())
        scoreRef.add(scoreMap)
            .addOnSuccessListener { Log.i("GameActivity", "Score saved successfully!") }
            .addOnFailureListener { e -> Log.e("GameActivity", "Error saving score: ${e.message}") }
    }

    private fun resetButtons(buttons: List<TextView>) {
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundColor(getColor(R.color.white))
        }
    }
}