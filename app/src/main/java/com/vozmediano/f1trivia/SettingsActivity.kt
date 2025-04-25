package com.vozmediano.f1trivia

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
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.vozmediano.f1trivia.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels { SettingsViewModel.Factory }


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

        val loginLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Snackbar.make(binding.root, "Login successful!", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Login canceled or failed", Snackbar.LENGTH_SHORT).show()
            }
        }


        binding.btnLogin.setOnClickListener {
            Log.i("SettingsActivity", "Login button clicked")
            val intent = Intent(this, LoginActivity::class.java)
            loginLauncher.launch(intent)
        }

        binding.btnRefreshData.setOnClickListener {
            try {
                viewModel.refreshData()
                Snackbar.make(it, "Data refreshed successfully", Snackbar.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.i("SettingsActivity", "Error refreshing data: ${e.message}")
            }
        }
    }
}