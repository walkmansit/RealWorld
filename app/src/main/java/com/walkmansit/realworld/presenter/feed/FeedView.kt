package com.walkmansit.realworld.presenter.feed

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineStops
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.walkmansit.realworld.domain.model.ArticleFilterType
import com.walkmansit.realworld.presenter.article.PaginatedLazyColumn
import com.walkmansit.realworld.presenter.feed.MAP.filterMapping
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedView(
    modifier: Modifier = Modifier,
//    navController: NavController = rememberNavController(),
    navigateArticle: (String) -> Unit,
    navigateNewArticle: () -> Unit,
    navigateLogin: () -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Log.d("FeedView", "FeedView recomposition")

    LaunchedEffect(key1 = true) {
        viewModel.uiState.collectLatest { event ->
            when (event.navEvent) {
                is FeedNavigationEvent.RedirectArticle ->
                    {
                        navigateArticle(event.navEvent.slug)
                        viewModel.onIntent(FeedIntent.RedirectComplete)
                    }
                is FeedNavigationEvent.RedirectNewArticle ->
                    {
                        navigateNewArticle()
                        viewModel.onIntent(FeedIntent.RedirectComplete)
                    }
                is FeedNavigationEvent.RedirectLogin ->
                    {
                        navigateLogin()
                        viewModel.onIntent(FeedIntent.RedirectComplete)
                    }
                is FeedNavigationEvent.Undefined -> { }
            }
        }
    }

    // State to track the scroll position
//    val listState = rememberLazyListState()
    // Coroutine scope for handling background operations like loading data
//    val coroutineScope = rememberCoroutineScope()
    // State to track if more items are being loaded
//    var isLoading by remember { mutableStateOf(false) }
    // Function to simulate loading more items (with a delay)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors =
                    topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                title = {
                    ArticleFilterMenu(
                        modifier,
                        uiState.selectedFilter,
                    ) { viewModel.onIntent(FeedIntent.LogOut) }
                },
                actions = {
                    IconButton(onClick = { viewModel.onIntent(FeedIntent.LogOut) }) {
                        Icon(
                            imageVector = Icons.Filled.AirlineStops,
                            contentDescription = "Logout",
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onIntent(FeedIntent.RedirectNewArticle) }) {
                Icon(Icons.Filled.Done, "Submit")
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column {
                PaginatedLazyColumn(
                    modifier,
                    viewModel.articlesResult,
                    onArticleClick = { article ->
                        viewModel.onIntent(FeedIntent.RedirectArticle(article.slug))
                    },
                    {},
//                    listState = listState,
//                    isLoading = isLoading
                )
            }
        }
    }
}

@Composable
fun ArticleFilterMenu(
    modifier: Modifier,
    selectedFilter: ArticleFilterType,
    onChangeFilter: (ArticleFilterType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "More options")
            }
            Text(filterMapping[selectedFilter]!!)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ArticleFilterType.entries.toTypedArray().onEach {
                DropdownMenuItem(
                    text = { Text(filterMapping[it]!!) },
                    onClick = {
                        onChangeFilter(it)
                        expanded = false
                    },
                )
            }
        }
    }
}

object MAP {
    val filterMapping: Map<ArticleFilterType, String> =
        mapOf(
            ArticleFilterType.MyArticles to "My Articles",
            ArticleFilterType.Feed to "Feed",
            ArticleFilterType.Explore to "Explore",
        )
}
