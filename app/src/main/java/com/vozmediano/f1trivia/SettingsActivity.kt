package com.vozmediano.f1trivia

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        binding.btnRefreshData.setOnClickListener {
            try {
                viewModel.refreshData()
                Snackbar.make(it, "Data refreshed successfully", Snackbar.LENGTH_LONG)
                    .show()
            } catch (e: Exception) {
                Log.i("SettingsActivity", "Error refreshing data: ${e.message}")
            }
        }
    }
}