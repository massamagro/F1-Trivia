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
import com.vozmediano.f1trivia.util.isValidUsername

class UsernameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsernameBinding
    private val db = FirebaseFirestore.getInstance()

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
            val username = binding.etUsername.text.toString().trim()
            val errorMessage = username.isValidUsername(1, 15)

            if (errorMessage == null) {
                checkUsernameAvailability(username)
            } else {
                binding.etUsername.error = errorMessage
            }
        }

    }

    private fun checkUsernameAvailability(username: String) {
        db.collection("usernames").document(username)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Username is already taken, display the error directly on the EditText
                    binding.etUsername.error = "This username is already taken"
                } else {
                    // Username is available, proceed to save it
                    saveUsername(username)
                }
            }
            .addOnFailureListener { e ->
                Log.e("UsernameActivity", "Error checking username availability: ${e.message}")
                binding.etUsername.error = "Error checking username. Please try again."
            }
    }

    private fun saveUsername(username: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            // Save the username to the main user document
            val userRef = db.collection("users").document(userId)
            val userMap = mapOf(
                "uid" to userId,
                "username" to username,
                "email" to user.email
            )
            userRef.set(userMap)
                .addOnSuccessListener {
                    Log.i("UsernameActivity", "Username saved to user document!")
                    // Also save the username in the 'usernames' collection for uniqueness check
                    db.collection("usernames").document(username).set(mapOf("uid" to userId))
                        .addOnSuccessListener {
                            Log.i("UsernameActivity", "Username saved to usernames collection!")
                            setResult(Activity.RESULT_OK)
                            finish() // Go back to LoginActivity
                        }
                        .addOnFailureListener { e ->
                            Log.e("UsernameActivity", "Error saving to usernames collection: ${e.message}")
                            // Consider deleting the username from 'users' if this fails
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("UsernameActivity", "Error saving to user document: ${e.message}")
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
        }
    }
}