package com.walkmansit.realworld.ui.article

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.walkmansit.realworld.UiEvent
import com.walkmansit.realworld.ui.shared.MultilineTextField
import com.walkmansit.realworld.ui.shared.RegularTextField
import kotlinx.coroutines.flow.collectLatest


@Composable
fun NewArticleView(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: NewArticleViewModel = hiltViewModel(),
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
                }

                is UiEvent.Undefined -> {}
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onIntent(NewArticleIntent.Submit) }) {
                Icon(Icons.Filled.Done, "Submit")
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
                text = "New article",
                fontSize = 26.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            RegularTextField(uiState.title, "Title") {
                viewModel.onIntent(NewArticleIntent.UpdateTitleIntent(it))
            }

            RegularTextField(uiState.description, "Description") {
                viewModel.onIntent(NewArticleIntent.UpdateDescriptionIntent(it))
            }

            MultilineTextField(uiState.body, "Body") {
                viewModel.onIntent(NewArticleIntent.UpdateBodyIntent(it))
            }

        }
    }

}