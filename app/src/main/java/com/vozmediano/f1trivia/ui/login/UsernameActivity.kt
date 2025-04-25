package com.vozmediano.f1trivia.ui.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vozmediano.f1trivia.databinding.ActivityUsernameBinding

class UsernameActivity : AppCompatActivity() {

    lateinit var binding: ActivityUsernameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUsernameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSubmit.setOnClickListener {
            val username = binding.etUsername.text.toString()

            if (username.isNotEmpty()) {
                saveUsername(username)
            } else {
                // Handle empty username case
                binding.etUsername.error = "Username cannot be empty"
            }
        }
    }

    fun saveUsername(username: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(userId)

            val userMap = mapOf(
                "uid" to userId,
                "username" to username,
                "email" to user.email
            )

            userRef.set(userMap)
                .addOnSuccessListener {
                    Log.i("UsernameActivity", "Username saved successfully!")
                    setResult(Activity.RESULT_OK)
                    finish() // Go back to LoginActivity
                }
                .addOnFailureListener { e ->
                    Log.e("UsernameActivity", "Error saving username: ${e.message}")
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
        }
    }

}
