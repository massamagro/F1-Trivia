package com.vozmediano.f1trivia.ui.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vozmediano.f1trivia.databinding.ActivityUsernameBinding
import com.vozmediano.f1trivia.util.isValidUsername

class ChangeUsernameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsernameBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var currentUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUsernameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadCurrentUsername()

        binding.btnSubmit.setOnClickListener {
            val newUsername = binding.etUsername.text.toString().trim()
            val errorMessage = newUsername.isValidUsername(1, 15)

            if (errorMessage == null) {
                if (newUsername != currentUsername) {
                    checkUsernameAvailability(newUsername)
                } else {
                    Toast.makeText(this, "Please enter a new username", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.etUsername.error = errorMessage
            }

        }
    }

    private fun loadCurrentUsername() {
        val userId = auth.currentUser?.uid
        Log.d("ChangeUsernameActivity", "loadCurrentUsername called for user ID: $userId")
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    currentUsername = document.getString("username")
                    Log.d("ChangeUsernameActivity", "Current username loaded: $currentUsername")
                    binding.etUsername.hint = "$currentUsername"
                }
                .addOnFailureListener { e ->
                    Log.e("ChangeUsernameActivity", "Error loading current username: ${e.message}")
                    Toast.makeText(this, "Error loading current username", Toast.LENGTH_SHORT)
                        .show()
                }
        } else {
            Log.d("ChangeUsernameActivity", "User ID is null, cannot load current username.")
        }
    }

    private fun checkUsernameAvailability(newUsername: String) {
        db.collection("usernames").document(newUsername)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    binding.etUsername.error = "This username is already taken"
                } else {
                    updateUsername(newUsername)
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "ChangeUsernameActivity",
                    "Error checking username availability: ${e.message}"
                )
                Toast.makeText(
                    this,
                    "Error checking username. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateUsername(newUsername: String) {
        val userId = auth.currentUser?.uid
        val oldUsername = currentUsername // Capture the current username

        if (userId != null && oldUsername != null) {
            db.runTransaction { transaction ->
                val userRef = db.collection("users").document(userId)
                val oldUsernameRef = db.collection("usernames").document(oldUsername)
                val newUsernameRef = db.collection("usernames").document(newUsername)

                // 1. Update the username in the user's document
                transaction.update(userRef, "username", newUsername)

                // 2. Delete the old username from the 'usernames' collection
                transaction.delete(oldUsernameRef)

                // 3. Add the new username to the 'usernames' collection
                transaction.set(newUsernameRef, mapOf("uid" to userId))

                null
            }.addOnSuccessListener {
                Log.i("ChangeUsernameActivity", "Username updated successfully!")
                Toast.makeText(this, "Username updated!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }.addOnFailureListener { e ->
                Log.e("ChangeUsernameActivity", "Error updating username: ${e.message}")
                Toast.makeText(
                    this,
                    "Error updating username. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}