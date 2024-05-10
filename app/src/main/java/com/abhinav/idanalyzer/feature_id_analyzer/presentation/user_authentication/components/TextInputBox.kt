package com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextInputBox(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            text = hint,
            color = Color.LightGray,
            fontSize = 13.sp,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.surface,
                errorTextColor = MaterialTheme.colorScheme.surface,
                errorBorderColor = MaterialTheme.colorScheme.onError,
                cursorColor = MaterialTheme.colorScheme.surface,
                errorCursorColor = MaterialTheme.colorScheme.onError,
            ),
            isError = isError,
            maxLines = 2,
        )
    }

}
