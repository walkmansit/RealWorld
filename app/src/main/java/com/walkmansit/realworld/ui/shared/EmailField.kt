package com.walkmansit.realworld.ui.shared

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
import com.walkmansit.realworld.common.TextFieldState

@Composable
fun EmailField(
    email: TextFieldState,
    onEmailChanged: (String) -> Unit,
) {
    Spacer(modifier = Modifier.height(32.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email.text,
        onValueChange = onEmailChanged,
        placeholder = {
            Text(text = "Email ")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
        isError = email.hasError()
    )
    if (email.hasError()) {
        Text(
            text = email.error ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}