package com.walkmansit.realworld.presenter.article


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
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkmansit.realworld.presenter.components.CircularProgress
import com.walkmansit.realworld.presenter.components.MultilineTextField
import com.walkmansit.realworld.presenter.components.RegularTextField
import com.walkmansit.realworld.presenter.components.RwScaffold
import com.walkmansit.realworld.presenter.components.TagsComponent
import com.walkmansit.realworld.presenter.components.TagsComponentSimple
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.api.IntentReceiver
import pro.respawn.flowmvi.compose.dsl.subscribe

@Composable
fun NewArticleView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    articleViewModel: NewArticleViewModel = hiltViewModel(),
    tagsViewModel: TagsViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) = with(articleViewModel.store) {
    val state by subscribe { action ->
        when (action) {
            is NewArticleAction.SubmitComplete -> {}
        }
    }

    RwScaffold(
        title = "New Article",
        upAvailable = navController.previousBackStackEntry != null,
        onUpClicked = { navController.popBackStack() },
        snackBarHostState = snackBarHostState,
        fab = {
            FloatingActionButton(onClick = { articleViewModel.store.intent(NewArticleIntent.Submit) }) {
                Icon(Icons.Filled.Done, "Submit")
            }
        },
    ) {
        NewArticleViewContainer(state, tagsViewModel)
    }
}

@Composable
fun IntentReceiver<NewArticleIntent>.NewArticleViewContainer(state: NewArticleState, tagsViewModel: TagsViewModel) {
    when(state){
        is NewArticleState.Loading -> CircularProgress()
        is NewArticleState.LoadingOnSubmit -> CircularProgress()
        is NewArticleState.Error -> Text(text = state.message)
        is NewArticleState.Content -> NewArticleViewContent(state.content, tagsViewModel)
    }
}

@Composable
fun IntentReceiver<TagsIntent>.TagsViewContainer(state: TagState) {
    when(state){
        is TagState.Loading -> CircularProgress()
        is TagState.Error -> Text(text = state.message)
        is TagState.Content -> TagsViewContent(state.content)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentReceiver<TagsIntent>.TagsViewContent(content : TagFields){
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    TagsComponent(
        content.selectedTags,
        true,
        { tag -> intent(TagsIntent.DeleteTag(tag)) },
        { showBottomSheet =  true},
    )

    val onHide = coroutineScope
        .launch { sheetState.hide() }
        .invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }

    TagsBottomSheet(
        showBottomSheet,
        sheetState,
        tagFields = content,
        {}
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

}

@Composable
fun IntentReceiver<NewArticleIntent>.NewArticleViewContent(content: NewArticleFields, tagsViewModel: TagsViewModel) {
    Column(
        modifier = Modifier
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

        RegularTextField(content.title, "Title") {
            intent(NewArticleIntent.UpdateTitle(it))
        }

        RegularTextField(content.description, "Description") {
            intent(NewArticleIntent.UpdateDescription(it))
        }

        MultilineTextField(content.body, "Body") {
            intent(NewArticleIntent.UpdateBody(it))
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

        with(tagsViewModel.store){
            val tagState by tagsViewModel.store.subscribe()
            TagsViewContainer(tagState)
        }

//        TagsComponent(content.selectedTags, true,
//            { viewModel.onIntent(NewArticleIntent.DeleteTag(it)) },
//            { showBottomSheet = true }
//        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentReceiver<TagsIntent>.TagsBottomSheet(
    showBottomSheet: Boolean,
    sheetState: SheetState,
    tagFields: TagFields,
    onHide: () -> Unit,
){
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
//                showBottomSheet = false
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
//                        .align(Alignment.Start)
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
                    IconButton(onClick = onHide
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowDownward, "hide")
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))
                TagsComponentSimple(tags = tagFields.selectedTags)
//                TagsComponent(tagFields.selectedTags, false,
//                    { viewModel.onIntent(NewArticleIntent.DeleteTag(it)) },
//                    { showBottomSheet = true}
//                )
                Spacer(modifier = Modifier.height(16.dp))
                SearchScreen(
                    { intent(TagsIntent.UpdateSearchQuery(it)) },
                    { intent(TagsIntent.SubmitNewTag(it)) },
//                    { intent(TagsIntent.DeleteTag(it)) },
                    { intent(TagsIntent.AddTag(it)) },
                    tagFields.searchQuery.text,
                    tagFields.searchResult,
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onUpdateQuery: (query: String) -> Unit,
    onSubmitTag: (tag: String) -> Unit,
//    onDeleteTag: (tag: String) -> Unit,
    onAddTag: (tag: String) -> Unit,
    searchQuery: String,
    searchResults: List<String>,
) {
//    val searchResults by searchResultsFlow.collectAsStateWithLifecycle()

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
                    onDone = { onSubmitTag(searchQuery) }
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
                key = { index -> searchResults[index] },
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
    tag: String,
    onAddTag: (tag: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(onClick = { onAddTag(tag) }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = tag)
        }
    }

}