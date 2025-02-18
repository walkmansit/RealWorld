package com.walkmansit.realworld

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SplashScreenViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    LaunchedEffect(key1 = true) {
        viewModel.uiState.collectLatest { event ->
            when (event.uiEvent) {
                is UiEvent.SnackbarEvent -> {
                    snackBarHostState.showSnackbar(
                        message = event.uiEvent.message,
                        duration = SnackbarDuration.Short
                    )
                }

                is UiEvent.NavigateEvent -> {
                    navController.navigate(event.uiEvent.route)
                    snackBarHostState.showSnackbar(
                        message = "Login Successful",
                        duration = SnackbarDuration.Short
                    )
                }

                is UiEvent.Undefined -> {}
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        CircularProgressIndicator(
            modifier = Modifier
                .padding(innerPadding)
                .width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}