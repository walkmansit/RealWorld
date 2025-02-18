package com.walkmansit.realworld.ui.shared

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.walkmansit.realworld.common.TextFieldState

@Composable
fun PasswordField(
    password: TextFieldState,
    onPasswordChanged: (String) -> Unit,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    Spacer(modifier = Modifier.height(32.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password.text,
        onValueChange = onPasswordChanged,
        placeholder = {
            Text(text = "Password ")
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Unspecified,
        ),
        isError = password.hasError(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff
            // Localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            // Toggle button to hide or display password
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        }

    )
    if (password.hasError()) {
        Text(
            text = password.error ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}