package com.vozmediano.f1trivia.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vozmediano.f1trivia.domain.model.Driver
import com.vozmediano.f1trivia.ui.theme.F1TriviaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Driver(viewModel)
        }
    }
}

@Composable
fun Driver(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is MainViewModel.DriverUiState.Loading -> Text(text = "Loading...", modifier = modifier)
        is MainViewModel.DriverUiState.Success -> {
            val driver = (uiState as MainViewModel.DriverUiState.Success).driver
            Text(
                text = "Driver: ${driver.familyName} ${driver.givenName}",
                modifier = modifier
            )
        }
        is MainViewModel.DriverUiState.Error -> {
            val error = (uiState as MainViewModel.DriverUiState.Error).exception
            Text(text = "Error: ${error.message}", modifier = modifier)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Driver() {
    F1TriviaTheme {
        Driver()
    }
}