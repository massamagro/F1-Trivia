package com.vozmediano.f1trivia.ui.mainmenu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vozmediano.f1trivia.databinding.ActivityMainMenuBinding
import com.vozmediano.f1trivia.ui.game.GameActivity
import com.vozmediano.f1trivia.ui.leaderboard.LeaderboardActivity
import com.vozmediano.f1trivia.ui.login.LoginActivity
import com.vozmediano.f1trivia.ui.settings.SettingsActivity

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var etLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        etLogin = binding.etLogin

        binding.btnPlay.setOnClickListener {
            val gameIntent = Intent(this, GameActivity::class.java)
            val loginIntent = Intent(this, LoginActivity::class.java)
            if (auth.currentUser != null) {
                startActivity(gameIntent)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Reminder")
                    .setMessage("Log in to save your score and join leaderboards!")
                    .setPositiveButton("Login") { _, _ ->
                        startActivity(loginIntent)
                    }
                    .setNegativeButton("Continue") { dialog, _ ->
                        dialog.dismiss()
                        startActivity(gameIntent)
                    }
                    .show()
            }
        }

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.btnLeaderboard.setOnClickListener {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        checkLoginStatus()
    }

    override fun onResume() {
        super.onResume()
        checkLoginStatus()
    }


    private fun checkLoginStatus() {
        val currentUser = auth.currentUser
        Log.i("MainMenuActivity", "(Check login status) Current user: $currentUser")
        if (currentUser != null) {
            // User is logged in, retrieve the username
            Log.i("MainMenuActivity", "(Check login status) User is logged in: ${currentUser?.uid}")
            retrieveUsername(currentUser.uid)
            etLogin.setOnClickListener {}
        } else {
            // User is not logged in
            Log.i("MainMenuActivity", "(Check login status) User is not logged in")
            etLogin.text = "Not logged in"
            etLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun retrieveUsername(uid: String) {
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists() && document.contains("username")) {
                    val username = document.getString("username")
                    etLogin.text = "Hi $username"
                    Log.i("MainMenuActivity", "(Retrieve username) Username: $username")
                } else {
                    Log.i("MainMenuActivity", "(Retrieve username) No username found for user: $uid")
                    etLogin.text = "Not logged in"
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainMenuActivity", "(Retrieve username) Error getting username: ", e)
                etLogin.text = "Not logged in"
            }
    }
}