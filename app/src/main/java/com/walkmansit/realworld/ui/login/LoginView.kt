package com.walkmansit.realworld.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.walkmansit.realworld.UiEvent
import com.walkmansit.realworld.ui.registration.RegistrationIntent
import com.walkmansit.realworld.ui.shared.EmailField
import com.walkmansit.realworld.ui.shared.PasswordField
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginView(
    modifier: Modifier = Modifier,
    navController : NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
//                    snackBarHostState.showSnackbar(
//                        message = "Login Successful",
//                        duration = SnackbarDuration.Short
//                    )
                }
                is UiEvent.Undefined -> { }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = { viewModel.onIntent(LoginIntent.Submit) }) {
                Icon(Icons.Filled.Done,"Submit")
            }
        }
    ) { padding ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Hello Again!",
                fontSize = 26.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Welcome Back you've been missed",
                fontSize = 19.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            //Email
            EmailField(uiState.email){
                viewModel.onIntent(LoginIntent.UpdateEmail(it))
            }

            //Password
            PasswordField(uiState.password){
                viewModel.onIntent(LoginIntent.UpdatePassword(it))
            }

            Spacer(modifier = Modifier.height(24.dp))

            var enabled by rememberSaveable{ mutableStateOf(true)}
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) {
                        enabled = false
                        viewModel.onIntent(LoginIntent.RedirectRegistration)
                    },
                text = "Create new account",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue,
                textAlign = TextAlign.Start,
            )

        }
    }

}