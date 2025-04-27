package com.vozmediano.f1trivia.ui.leaderboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.vozmediano.f1trivia.databinding.ActivityLeaderboardBinding
import com.vozmediano.f1trivia.domain.model.quiz.ScoreboardEntry
import com.vozmediano.f1trivia.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvLeaderboard.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.Main).launch {
            loadUserBestScore()
            setupLeaderboardTypeSpinner()
        }
    }

    private fun setupLeaderboardTypeSpinner() {
        val leaderboardTypes = arrayOf("All-Time", "Monthly")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, leaderboardTypes)
        binding.spinnerLeaderboardType.adapter = adapter

        binding.spinnerLeaderboardType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = leaderboardTypes[position]
                binding.tvLeaderboardTitle.text = "Top 10 $selectedType Scores:"
                loadLeaderboardData(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //no action
            }
        }
    }

    private fun loadLeaderboardData(leaderboardType: String) {
        CoroutineScope(Dispatchers.Main).launch {
            when (leaderboardType) {
                "All-Time" -> loadTopAllTimeScores()
                "Monthly" -> loadTopMonthlyScores()
            }
        }
    }

    private suspend fun loadUserBestScore() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val snapshot = firestore.collection("scores")
                    .whereEqualTo("uid", userId)
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()

                if (!snapshot.isEmpty) {
                    val bestScore = snapshot.documents.first().getLong("score")
                    binding.tvUserBestScore.text = bestScore.toString()
                } else {
                    binding.tvUserBestScore.text = "0"
                }
            } catch (e: Exception) {
                Log.e("LeaderboardActivity", "Error loading user best score: ${e.message}")
                binding.tvUserBestScore.text = "Error"
            }
        } else {
            binding.tvUserBestScore.text = "Log In" // Or handle unauthenticated state
            binding.tvUserBestScore.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private suspend fun loadTopAllTimeScores() {
        try {
            val snapshot = firestore.collection("scores")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val scoresList = mutableListOf<ScoreboardEntry>()
            for (document in snapshot.documents) {
                val uid = document.getString("uid")
                val score = document.getLong("score")?.toInt() ?: 0
                val timestamp = document.getLong("timestamp")?.toInt() ?: 0
                if (uid != null) {
                    val username = fetchUsername(uid)
                    scoresList.add(ScoreboardEntry(username ?: "Unknown", score, timestamp))
                }
            }
            binding.rvLeaderboard.adapter = ScoreboardAdapter(scoresList)
        } catch (e: Exception) {
            Log.e("LeaderboardActivity", "Error loading all-time scores: ${e.message}")
            binding.rvLeaderboard.adapter = ScoreboardAdapter(emptyList())
        }
    }

    private suspend fun loadTopMonthlyScores() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"))
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)

        val calendarStart = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"))
        calendarStart.set(currentYear, currentMonth, 1, 0, 0, 0)
        calendarStart.set(Calendar.MILLISECOND, 0)
        val startOfMonthTimestamp = calendarStart.timeInMillis

        val calendarEnd = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"))
        calendarEnd.set(currentYear, currentMonth, calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
        calendarEnd.set(Calendar.MILLISECOND, 999)
        val endOfMonthTimestamp = calendarEnd.timeInMillis

        try {
            val snapshot = firestore.collection("scores")
                .whereGreaterThanOrEqualTo("timestamp", startOfMonthTimestamp)
                .whereLessThanOrEqualTo("timestamp", endOfMonthTimestamp)
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val scoresList = mutableListOf<ScoreboardEntry>()
            for (document in snapshot.documents) {
                val uid = document.getString("uid")
                val score = document.getLong("score")?.toInt() ?: 0
                val timestamp = document.getLong("timestamp")?.toInt() ?: 0
                if (uid != null) {
                    val username = fetchUsername(uid)
                    scoresList.add(ScoreboardEntry(username ?: "Unknown", score, timestamp))
                }
            }
            binding.rvLeaderboard.adapter = ScoreboardAdapter(scoresList)
        } catch (e: Exception) {
            Log.e("LeaderboardActivity", "Error loading current monthly scores: ${e.message}")
            binding.rvLeaderboard.adapter = ScoreboardAdapter(emptyList())
        }
    }

    private suspend fun fetchUsername(uid: String): String? {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            document.getString("username")
        } catch (e: Exception) {
            Log.e("LeaderboardActivity", "Error fetching username for $uid: ${e.message}")
            null
        }
    }
}