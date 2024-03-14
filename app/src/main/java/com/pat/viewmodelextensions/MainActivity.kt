package com.pat.viewmodelextensions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pat.viewmodelextensions.ui.theme.ViewModelExtensionsTheme
import com.pat.viewmodelextensions.viewmodel.MainViewModel
import com.pat.viewmodelextensions.viewmodel.UiAction
import com.pat.viewmodelextensions.viewmodel.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runTimer(coroutineScope = CoroutineScope(Dispatchers.Default))

        setContent {
            ViewModelExtensionsTheme {
                SampleAppScreen()
            }
        }
    }

    @Composable
    private fun SampleAppScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            viewModel.runWithState {
                when (it) {
                    UiState.Loading -> {
                        CircularProgressIndicator()
                    }

                    UiState.Loaded -> {
                        Text(text = "Loaded", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }

    private fun runTimer(coroutineScope: CoroutineScope) {
        timer(initialDelay = 3000, period = 1000) {
            coroutineScope.launch {
                viewModel.emitAction(UiAction.UpdateState(UiState.Loaded))
            }
            cancel()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ViewModelExtensionsTheme {
            SampleAppScreen()
        }
    }
}