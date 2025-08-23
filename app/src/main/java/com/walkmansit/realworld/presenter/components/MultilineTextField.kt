package com.walkmansit.realworld.presenter.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MultilineTextField(
    value: TextFieldState,
    placeholderText: String,
    minLines: Int = 3,
    maxLines: Int = 5,
    onValueChanged: (String) -> Unit,
) {
    Spacer(modifier = Modifier.height(32.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value.text,
        minLines = minLines,
        maxLines = maxLines,
        onValueChange = onValueChanged,
        placeholder = {
            Text(text = placeholderText)
        },
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
        isError = value.hasError(),
    )
    if (value.hasError()) {
        Text(
            text = value.error ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
