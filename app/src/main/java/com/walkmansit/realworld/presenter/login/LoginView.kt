package com.walkmansit.realworld.presenter.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkmansit.realworld.presenter.components.CircularProgress
import com.walkmansit.realworld.presenter.components.EmailField
import com.walkmansit.realworld.presenter.components.PasswordField
import com.walkmansit.realworld.presenter.components.RwScaffold
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe

@Composable
fun LoginView(
    navController: NavController = rememberNavController(),
    navigateRegistration: () -> Unit,
    navigateFeed: (String) -> Unit,
    toast: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) = with(viewModel.store) {
    val state by subscribe { action ->
        when (action) {
            is LoginAction.RedirectRegistration -> navigateRegistration()
            is LoginAction.RedirectFeed -> navigateFeed(action.username)
            is LoginAction.ShowMessage -> toast(action.text)
        }
    }

    RwScaffold(
        title = "Login",
        upAvailable = navController.previousBackStackEntry != null,
        onUpClicked = { navController.popBackStack() },
        snackBarHostState = snackBarHostState,
        fab = {
            SmallFloatingActionButton(onClick = { intent(LoginIntent.SubmitStart) }) {
                Icon(Icons.Filled.Done, "Submit")
            }
        },
    ) {
        LoginViewContainer(state)
    }
}

@Composable
fun IntentReceiver<LoginIntent>.LoginViewContainer(state: LoginState) {
    when (state) {
        is LoginState.Loading -> CircularProgress()
        is LoginState.LoadingOnSubmit -> CircularProgress()
        is LoginState.Error -> Text(text = state.message)
        is LoginState.Content -> LoginViewContent(state)
    }
}

@Composable
fun IntentReceiver<LoginIntent>.LoginViewContent(state: LoginState.Content) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Hello Again!",
            fontSize = 26.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Welcome Back you've been missed",
            fontSize = 19.sp,
            color = Color.Black,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email
        EmailField(state.fields.email) {
            intent(LoginIntent.UpdateEmail(it))
        }

        // Password
        PasswordField(state.fields.password) {
            intent(LoginIntent.UpdatePassword(it))
        }

        Spacer(modifier = Modifier.height(24.dp))

        var enabled by rememberSaveable { mutableStateOf(true) }
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) {
                        enabled = false
                        intent(LoginIntent.RedirectRegistration)
                    },
            text = "Create new account",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Blue,
            textAlign = TextAlign.Start,
        )
    }
}
