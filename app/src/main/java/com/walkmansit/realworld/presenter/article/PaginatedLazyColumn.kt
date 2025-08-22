package com.walkmansit.realworld.presenter.article

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.walkmansit.realworld.domain.model.Article
import com.walkmansit.realworld.presenter.components.CircleAvatar
import kotlinx.coroutines.flow.Flow

@Composable
fun PaginatedLazyColumn(
    modifier: Modifier = Modifier,
    pagerFlow:  Flow<PagingData<Article>>,
    onArticleClick: (Article) -> Unit,
    addFavorite: (Article) -> Unit,
//    items: PersistentList<String>,  // Using PersistentList for efficient state management
//    loadMoreItems: () -> Unit,  // Function to load more items
//    listState: LazyListState,  // Track the scroll state of the LazyColumn
//    isLoading: Boolean,  // Track if items are being loaded

) {
    Log.d("PaginatedLazyColumn", "PaginatedLazyColumn view recomposition")

    val lazyPagingItems: LazyPagingItems<Article> = pagerFlow.collectAsLazyPagingItems()

    LazyColumn(
//        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = modifier

            .fillMaxSize()
            .padding(16.dp),  // Add padding for better visual spacing
    ) {
        items(
            lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it }
        ) { index ->
            val item = lazyPagingItems[index]!!

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .clickable { onArticleClick(item) }
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val (favorite, title, avatar) = createRefs()

                    IconButton(
                        modifier = Modifier
                            .constrainAs(favorite){
                                start.linkTo(parent.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
,
                        onClick = { addFavorite(item) },
//                        modifier = Modifier.align(Alignment.Top)
                    ) {
                        Icon(
                            imageVector = if (item.favorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            "Favorite icon"
                        )

                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .constrainAs(title){
                                start.linkTo(favorite.end, margin = 16.dp)
                                end.linkTo(avatar.start, margin = 16.dp)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
//                            .fillParentMaxWidth()
//                            .fillMaxWidth()
                    ) {

                        Text(
                            text = item.title,
                            textAlign = TextAlign.Start,
                        )
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = item.description,
                            textAlign = TextAlign.Start,
                        )
                    }
                    CircleAvatar(
                        item.slug, item.author.username,
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(avatar) {
                                end.linkTo(parent.end, margin = 16.dp)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            }
                    )

                }
            }
            if(index < lazyPagingItems.itemCount)
                Spacer(modifier = Modifier.height(16.dp))

//            Text(text = item.slug, modifier = Modifier.padding(8.dp))  // Add padding to each item
        }

//         Render each item in the list using a unique key
//        itemsIndexed(lazyPagingItems, key = { it, item -> item }) { index, item ->
//            Text(text = item, modifier = Modifier.padding(8.dp))  // Add padding to each item
//        }

//            // Check if we've reached the end of the list
//            if (index == items.lastIndex && !isLoading) {
//                loadMoreItems()
//            }

        // Show a loading indicator at the bottom when items are being loaded
//        if (isLoading) {
//            item {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()  // Display a circular loading indicator
//                }
//            }
//        }
    }
}