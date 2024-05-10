package com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SignUpComponent(
    modifier : Modifier = Modifier,
    onSubmit : (String, String, String, String) -> Unit,
    isError : Boolean,
    errorMessage : String,
    onValueChange : () -> Unit,
    isLoading: Boolean = false
) {

    var name by remember { mutableStateOf("") }
    var username by remember{mutableStateOf("")}
    var password by remember{mutableStateOf("")}
    var confirmPassword by remember{mutableStateOf("")}

    Column(
        modifier = modifier
    ){
        TextInputBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = name,
            hint = "name",
            isError = isError,
            onValueChange ={
                name = it
                onValueChange()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextInputBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = username,
            hint = "email id",
            isError = isError,
            onValueChange = {
                username = it
                onValueChange()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextInputBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = password,
            hint = "password",
            isError = isError,
            onValueChange = {
                password = it
                onValueChange()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextInputBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = confirmPassword,
            hint = "retype password",
            isError = isError,
            onValueChange = {
                confirmPassword = it
                onValueChange()
            }
        )

        if(isError){
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = errorMessage,
                color = MaterialTheme.colorScheme.onError,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = {
                onSubmit(name, username, password, confirmPassword)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Sign up",
                color = MaterialTheme.colorScheme.surface
            )
        }

    }

    if (isLoading){
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