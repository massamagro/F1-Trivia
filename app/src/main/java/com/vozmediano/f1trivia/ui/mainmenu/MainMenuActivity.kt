package com.vozmediano.f1trivia.ui.mainmenu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vozmediano.f1trivia.databinding.ActivityMainMenuBinding
import com.vozmediano.f1trivia.ui.game.GameActivity
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
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etLogin = binding.etLogin

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        Log.i("MainMenuActivity", "onCreate called")
        checkLoginStatus() // Initial check when the activity is created
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainMenuActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainMenuActivity", "onResume called")
        checkLoginStatus() // Re-check login status every time the activity resumes
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainMenuActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainMenuActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainMenuActivity", "onDestroy called")
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
                    etLogin.text = "Not logged in" // Or handle this case differently
                }
            }
            .addOnFailureListener { e ->
                Log.e("MainMenuActivity", "(Retrieve username) Error getting username: ", e)
                etLogin.text = "Not logged in" // Or handle this error differently
            }
    }
}