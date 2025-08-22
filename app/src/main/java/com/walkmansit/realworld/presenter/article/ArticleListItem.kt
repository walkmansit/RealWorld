package com.walkmansit.realworld.presenter.article

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.walkmansit.realworld.domain.model.Article


@Composable
fun ArticleListItem(
    article: Article,
    modifier: Modifier = Modifier,
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier.fillMaxWidth()

    ) {
        Text(
            text = article.slug,
            modifier = modifier
                .padding(16.dp),
            textAlign = TextAlign.Start,
        )
    }
}