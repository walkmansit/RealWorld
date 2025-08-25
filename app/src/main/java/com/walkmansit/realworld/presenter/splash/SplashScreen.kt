package com.walkmansit.realworld.presenter.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.walkmansit.realworld.presenter.components.CircularProgress
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateLogin: () -> Unit,
    navigateRegistration: () -> Unit,
    navigateFeed: (String) -> Unit,
    viewModel: SplashScreenViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
//    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiState.collectLatest { event ->
            when (event.navEvent) {
                is SplashNavigationEvent.RedirectRegistration -> {
                    navigateRegistration()
                    viewModel.consumeNavEvent()
                }

                is SplashNavigationEvent.RedirectLogin -> {
                    navigateLogin()
                    viewModel.consumeNavEvent()
                }

                is SplashNavigationEvent.RedirectFeed -> {
                    navigateFeed(event.navEvent.username)
                    viewModel.consumeNavEvent()
                }

                is SplashNavigationEvent.Undefined -> {}
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) {
        CircularProgress()
    }
}
