package com.walkmansit.realworld.presenter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsComponent(
    tags: List<String>,
    showSearch: Boolean = false,
    onDelete: (tag: String) -> Unit = {},
    onShowSearch: () -> Unit = {},
){

    FlowRow(
        maxLines = 2,
        horizontalArrangement = Arrangement.Start, // Arrangement.spacedBy(2.dp)
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(2.dp)
    ) {
        tags.forEach { tag ->
            TextChipWithIcon(tag, showSearch, onDelete)
        }
        if (showSearch) {
            TextChipEdit("Edit", onShowSearch)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsComponentSimple(
    tags: List<String>,
    onDelete: (tag: String) -> Unit = {},
){

    FlowRow(
        maxLines = 2,
        horizontalArrangement = Arrangement.Start, // Arrangement.spacedBy(2.dp)
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(2.dp)
    ) {
        tags.forEach { tag ->
            TextChipWithIcon(tag,false, onDelete)
        }
    }
}

@Composable
fun TextChipWithIcon(
    tag: String,
    canDelete: Boolean,
    onDelete: (tag: String) -> Unit,
) {
    val shape = CircleShape
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                vertical = 2.dp,
                horizontal = 2.dp
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = shape
            )
            .background(
                color = White,
                shape = shape
            )
            .clip(shape = shape)
            .padding(2.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(all = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = tag,
            color = Color.Black
        )
        if (canDelete) {
            IconButton(onClick = { onDelete(tag) }) {
                Icon(imageVector = Icons.Filled.Done, "delete tag")
            }
        }
    }
}

@Composable
fun TextChipEdit(
    text: String,
    onEdit: () -> Unit,
) {
    Button(onClick = onEdit) {
        Text(

            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = text,
            color = White
        )

    }
}