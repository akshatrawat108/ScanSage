package com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AuthLogInComponent(
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit,
    errorMessage : String,
    isError: Boolean = false,
    isLoading: Boolean = false,
    onValueChange: () -> Unit,
    onBeginSignUp : () -> Unit
) {
    var usernameString by remember { mutableStateOf("") }
    var passwordString by remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        TextInputBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = usernameString,
            hint = "username",
            onValueChange = { it ->
                usernameString = it
                onValueChange()
            },
            isError = isError
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextInputBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = passwordString,
            hint = "password",
            onValueChange = { it ->
                passwordString = it
                onValueChange()
            },
            isError = isError
        )
        if(isError) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        TextButton(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { onBeginSignUp() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = "Don't have an account? Create one here",
                textAlign = TextAlign.Start,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = {
                onSubmit(usernameString, passwordString)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Log in",
                color = MaterialTheme.colorScheme.surface
            )
        }
    }

    if(isLoading){
       Dialog(
           onDismissRequest = {},
           properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
       ) {
           CircularProgressIndicator(
               color = MaterialTheme.colorScheme.secondary
           )
       }
    }
}



@Preview
@Composable
fun AuthLogInComponentPreview() {
    AuthLogInComponent(
        onSubmit = { _, _ ->

        },
        modifier = Modifier.fillMaxSize(),
        errorMessage = "",
        onValueChange = {

        },
        onBeginSignUp = {

        }
    )
}