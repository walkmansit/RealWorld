package com.walkmansit.realworld.ui.article

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkmansit.realworld.ui.shared.CircularProgress
import com.walkmansit.realworld.ui.shared.ErrorMessage
import com.walkmansit.realworld.ui.shared.RwScaffold
import com.walkmansit.realworld.ui.shared.TagsComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleView(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: ArticleViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

//    LaunchedEffect(key1 = true) {
//        viewModel.uiState.collectLatest { event ->
//            when (event.uiEvent) {
//                is UiEvent.SnackbarEvent -> {
//                    snackBarHostState.showSnackbar(
//                        message = event.uiEvent.message,
//                        duration = SnackbarDuration.Short
//                    )
//                }
//
//                is UiEvent.NavigateEvent -> {
//                    navController.navigate(event.uiEvent.route)
//                }
//
//                is UiEvent.Undefined -> {}
//            }
//        }
//    }


    RwScaffold(
        title = "Article details",
        upAvailable = navController.previousBackStackEntry != null,
        onUpClicked = { navController.popBackStack() },
        snackBarHostState = snackBarHostState,
    ) { padding ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        when(uiState){
            is ArticleUiState.IsLoading -> { CircularProgress() }
            is ArticleUiState.HasError -> { ErrorMessage(message = (uiState as ArticleUiState.HasError).errorMsg) }
            is ArticleUiState.ArticleUiData -> {
                ViewArticle(
                    modifier,
                    uiState as ArticleUiState.ArticleUiData,
                    padding,
            ) }
        }
    }
}

@Composable
fun ViewArticle(
    modifier: Modifier,
    article: ArticleUiState.ArticleUiData,
    padding: PaddingValues,
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(
//            modifier = Modifier.fillMaxWidth(),
//            text = "View article",
//            fontSize = 26.sp,
//            color = Color.Black,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center
//        )
        //Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = "Title",
            fontSize = 22.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = article.title.text,
            fontSize = 26.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )

        //Description
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = "Description",
            fontSize = 22.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = article.description.text,
            fontSize = 26.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )

        //Body
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = "Body",
            fontSize = 22.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = article.body.text,
            fontSize = 26.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )

        //Tags
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Tags",
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(24.dp))

        TagsComponent(article.selectedTags)
    }
}