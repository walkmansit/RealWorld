package com.walkmansit.realworld.ui.article

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.walkmansit.realworld.UiEvent
import com.walkmansit.realworld.domain.model.Tag
import com.walkmansit.realworld.ui.shared.MultilineTextField
import com.walkmansit.realworld.ui.shared.RegularTextField
import com.walkmansit.realworld.ui.shared.TagsComponent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.walkmansit.realworld.ui.shared.RwScaffold as RwScaffold1


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewArticleView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: NewArticleViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(key1 = true) {
        viewModel.uiState.collectLatest { event ->
            when (event.uiEvent) {
                is UiEvent.SnackBarEvent -> {
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

    RwScaffold1(
        title = "New Article",
        upAvailable = navController.previousBackStackEntry != null,
        onUpClicked = { navController.popBackStack() },
        snackBarHostState = snackBarHostState,
        fab = {
            FloatingActionButton(onClick = { viewModel.onIntent(NewArticleIntent.Submit) }) {
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Tags",
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            TagsComponent(uiState.selectedTags, true,
                { viewModel.onIntent(NewArticleIntent.DeleteTag(it)) },
                { showBottomSheet = true}
            )


            BackHandler(sheetState.isVisible) {
                coroutineScope.launch { sheetState.hide() }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 16.dp,
                    dragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .width(50.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.primary)
                                .align(Alignment.Start)
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "Tags",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = {
                                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Filled.ArrowDownward, "hide")
                            }

                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        TagsComponent(uiState.selectedTags, false,
                            { viewModel.onIntent(NewArticleIntent.DeleteTag(it)) },
                            { showBottomSheet = true}
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SearchScreen(
                            { viewModel.onIntent(NewArticleIntent.UpdateSearchQueryIntent(it)) },
                            { viewModel.onIntent(NewArticleIntent.SubmitTag) },
                            { viewModel.onIntent(NewArticleIntent.DeleteTag(it)) },
                            { viewModel.onIntent(NewArticleIntent.AddTag(it)) },
                            uiState.searchQuery.text,
                            viewModel.searchResult,
                        )
                    }
                }
            }


        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onUpdateQuery: (query: String) -> Unit,
    onSubmitTag: () -> Unit,
    onDeleteTag: (tag: Tag) -> Unit,
    onAddTag: (tag: Tag) -> Unit,
    searchQuery: String,
    searchResultsFlow: StateFlow<List<Tag>>
) {
    val searchResults by searchResultsFlow.collectAsStateWithLifecycle()

    val onActiveChange : (Boolean) -> Unit = {
    }
    val colors1 = SearchBarDefaults.colors()
    SearchBar(
        inputField = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = onUpdateQuery,

                placeholder = {
                    Text(text = "Search ")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onSubmitTag() }
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }

            )

//            SearchBarDefaults.InputField(
//                query = searchQuery,
//                onQueryChange = onUpdateQuery,
//                onSearch = {},
//                expanded = false,
//                onExpandedChange = onActiveChange,
//                enabled = true,
//                placeholder = {
//                    Text(text = "Search or submit")
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        tint = MaterialTheme.colorScheme.onSurface,
//                        contentDescription = null
//                    )
//                },
//                trailingIcon = {},
//                colors = colors1.inputFieldColors,
//                interactionSource = null,
//            )
        },
        expanded = true,
        onExpandedChange = onActiveChange,
        shape = SearchBarDefaults.fullScreenShape,
        colors = colors1,
        tonalElevation = 0.dp,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        windowInsets = SearchBarDefaults.windowInsets,

        ){
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = searchResults.size,
                key = { index -> searchResults[index].id },
                itemContent = { index ->
                    val tag = searchResults[index]
                    TagListItem(tag = tag, onAddTag)
                }
            )
        }

    }
}

@Composable
fun TagListItem(
    tag: Tag,
    onAddTag: (tag: Tag) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(onClick = { onAddTag(tag) }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = tag.value)
        }
    }

}