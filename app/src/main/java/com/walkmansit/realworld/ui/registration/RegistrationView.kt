package com.walkmansit.realworld.ui.registration

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkmansit.realworld.common.TextFieldState
import com.walkmansit.realworld.ui.shared.EmailField
import com.walkmansit.realworld.ui.shared.PasswordField
import com.walkmansit.realworld.ui.shared.RwScaffold
import kotlinx.coroutines.flow.collectLatest


@Composable
fun RegistrationView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    navigateLogin: () -> Unit,
    navigateFeed: (String) -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    LaunchedEffect(key1 = true) {
        viewModel.uiState.collectLatest { event ->
            when (event.navEvent) {
                is RegNavigationEvent.RedirectLogin ->
                {
                    navigateLogin()
                    viewModel.onIntent(RegistrationIntent.RedirectComplete)
                }
                is RegNavigationEvent.RedirectFeed ->
                {
                    navigateFeed(event.navEvent.username)
                    viewModel.onIntent(RegistrationIntent.RedirectComplete)
                }
                is RegNavigationEvent.Undefined -> { }
            }
        }
    }

    RwScaffold(
        title = "",
        upAvailable = navController.previousBackStackEntry != null,
        onUpClicked = { navController.popBackStack() },
        snackBarHostState = snackBarHostState,
        fab = {
            SmallFloatingActionButton(onClick = { viewModel.onIntent(RegistrationIntent.Submit) }) {
                Icon(Icons.Filled.Done, "Submit")
            }
        },
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
                text = "Welcome!",
                fontSize = 26.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Register an account with Us",
                fontSize = 19.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )

            //Username
            UserNameField(uiState.username, "Username ") {
                viewModel.onIntent(RegistrationIntent.UpdateUserName(it))
            }

            //Email
            EmailField(uiState.email) {
                viewModel.onIntent(RegistrationIntent.UpdateEmail(it))
            }

            //Password
            PasswordField(uiState.password) {
                viewModel.onIntent(RegistrationIntent.UpdatePassword(it))
            }

            Spacer(modifier = Modifier.height(24.dp))

            var enabled by rememberSaveable { mutableStateOf(true) }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) {
                        enabled = false
                        viewModel.onIntent(RegistrationIntent.RedirectLogin)
                    },
                text = "Already have an account? Sign in",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue,
                textAlign = TextAlign.Start,
            )
        }
    }

}

@Composable
fun UserNameField(
    username: TextFieldState,
    placeholderText: String,
    onUserNameChanged: (String) -> Unit,
) {
    Spacer(modifier = Modifier.height(32.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = username.text,
        onValueChange = onUserNameChanged,
        placeholder = {
            Text(text = placeholderText)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
        isError = username.hasError()
    )
    if (username.hasError()) {
        Text(
            text = username.error ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}