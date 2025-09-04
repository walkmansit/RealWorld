package com.walkmansit.realworld.presenter.feed

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.walkmansit.realworld.domain.model.ArticleFilterType
import com.walkmansit.realworld.presenter.article.PaginatedLazyColumn
import com.walkmansit.realworld.presenter.components.CircularProgress
import com.walkmansit.realworld.presenter.feed.MAP.filterMapping
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe

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
) = with(viewModel.store) {
    Log.d("FeedView", "FeedView recomposition")

    val state by subscribe { action ->
        when (action) {
            is FeedAction.RedirectLogin -> navigateLogin()
            is FeedAction.RedirectArticle -> navigateArticle(action.slug)
            is FeedAction.RedirectNewArticle -> navigateNewArticle()
        }
    }

//    LaunchedEffect(key1 = true) {
//        viewModel.uiState.collectLatest { event ->
//            when (event.navEvent) {
//                is FeedAction.RedirectArticle -> {
//                    navigateArticle(event.navEvent.slug)
//                    viewModel.onIntent(FeedIntent.RedirectComplete)
//                }
//
//                is FeedAction.RedirectNewArticle -> {
//                    navigateNewArticle()
//                    viewModel.onIntent(FeedIntent.RedirectComplete)
//                }
//
//                is FeedAction.RedirectLogin -> {
//                    navigateLogin()
//                    viewModel.onIntent(FeedIntent.RedirectComplete)
//                }
//
//                is FeedAction.Undefined -> {}
//            }
//        }
//    }

    // State to track the scroll position
//    val listState = rememberLazyListState()
    // Coroutine scope for handling background operations like loading data
//    val coroutineScope = rememberCoroutineScope()
    // State to track if more items are being loaded
//    var isLoading by remember { mutableStateOf(false) }
    // Function to simulate loading more items (with a delay)

//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                        state.filter,
                    ) {
                        intent(FeedIntent.ChangeFilter(it))
                    }
                },
                actions = {
                    IconButton(onClick = { intent(FeedIntent.LogOut) }) {
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
            FloatingActionButton(onClick = { intent(FeedIntent.RedirectNewArticle) }) {
                Icon(Icons.Filled.Done, "Submit")
            }
        },
    ) { padding ->
        FeedViewContainer(padding, state)
    }
}

@Composable
fun IntentReceiver<FeedIntent>.FeedViewContainer(
    padding: PaddingValues,
    state: FeedState,
) {
    when (state) {
        is FeedState.Loading -> CircularProgress()
        is FeedState.LoadingOnSubmit -> CircularProgress()
        is FeedState.Error -> Text(text = state.message)
        is FeedState.Content -> FeedViewContent(padding, state)
    }
}

@Composable
fun IntentReceiver<FeedIntent>.FeedViewContent(
    padding: PaddingValues,
    state: FeedState.Content,
) {
    Box(modifier = Modifier.padding(padding)) {
        Column {
            PaginatedLazyColumn(
//                modifier,
                pagerFlow = state.articles,
                onArticleClick = { article ->
                    intent(FeedIntent.RedirectArticle(article.slug))
                },
                addFavorite = { _ -> {} },
            )
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
