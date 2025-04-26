package com.vozmediano.f1trivia.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.vozmediano.f1trivia.ui.login.ChangeUsernameActivity
import com.vozmediano.f1trivia.databinding.ActivitySettingsBinding
import com.vozmediano.f1trivia.ui.login.LoginActivity

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels { SettingsViewModel.Factory }
    private val auth = FirebaseAuth.getInstance()
    private lateinit var loginLauncher: androidx.activity.result.ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loginLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                updateLoginButtonState() // Update button after successful login
                Snackbar.make(binding.root, "Login successful!", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Login canceled or failed", Snackbar.LENGTH_SHORT).show()
            }
        }

        updateLoginButtonState() // Initial state of the button

        binding.btnLogin.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                // User is not logged in, launch login activity
                Log.i("SettingsActivity", "Login button clicked")
                val intent = Intent(this, LoginActivity::class.java)
                loginLauncher.launch(intent)
            } else {
                // User is logged in, perform logout
                Log.i("SettingsActivity", "Logout button clicked")
                auth.signOut()
                // Optional: Sign out from Google as well
                GoogleSignIn.getClient(
                    this,
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut().addOnCompleteListener {
                    updateLoginButtonState() // Update button after logout
                    Snackbar.make(binding.root, "Logged out successfully", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRefreshData.setOnClickListener {
            try {
                viewModel.refreshData()
                Snackbar.make(it, "Data refreshed successfully", Snackbar.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.i("SettingsActivity", "Error refreshing data: ${e.message}")
            }
        }

        binding.btnChangeUsername.setOnClickListener {
            val intent = Intent(this, ChangeUsernameActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateLoginButtonState() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            binding.btnLogin.text = "Login"
            binding.btnChangeUsername.isEnabled = false
        } else {
            binding.btnLogin.text = "Logout"
            binding.btnChangeUsername.isEnabled = true
        }
    }
}