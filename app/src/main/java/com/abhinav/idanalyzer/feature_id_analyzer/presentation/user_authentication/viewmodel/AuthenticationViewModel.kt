package com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhinav.idanalyzer.core.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    private var _state = MutableStateFlow(AuthenticationState())
    val state = _state.asStateFlow()

    private var _credentialsValidationState =
        MutableStateFlow(CredentialsValidate())
    val credentialsValidateState = _credentialsValidationState.asStateFlow()

    fun onAction(action: AuthenticationEvent) {
        when (action) {
            is AuthenticationEvent.LogInWithEmail -> {

                if (!validateLogInCredentials(action.username, action.password)) {
                    return
                }

                viewModelScope.launch(Dispatchers.Main){
                    try {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                        withContext(Dispatchers.IO) {
                            val app = App.create(APP_ID)
                            app.login(
                                Credentials.emailPassword(
                                    action.username,
                                    action.password
                                )
                            )
                            _state.value = _state.value.copy(
                                isLoading = false,
                                isAuthenticated = app.currentUser != null
                            )
                        }

                    } catch (e: Exception) {
                        Log.e("AuthViewModel", e.message.toString())
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isAuthenticated = false
                        )
                        _credentialsValidationState.value = _credentialsValidationState.value.copy(
                            isError = true,
                            errorMessage = e.message.toString()
                        )
                    }
                }
            }

            is AuthenticationEvent.SignInWithEmail -> {
                if(! validateSignInCredentials(
                    action.name,action.email,action.password,action.confirmPassword
                )){
                    return
                }

                viewModelScope.launch(Dispatchers.Main){
                    try {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                        withContext(Dispatchers.IO){
                            val app = App.create(APP_ID)
                            app.emailPasswordAuth.registerUser(action.email, action.password)
                            delay(600)
                            app.login(Credentials.emailPassword(action.email, action.password))
                            _state.value = _state.value.copy(
                                isLoading = false,
                                isAuthenticated = app.currentUser != null
                            )
                        }
                    }catch (e: Exception){
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isAuthenticated = false
                        )
                        Log.e("AuthViewModel" , e.message.toString())
                        _credentialsValidationState.value = _credentialsValidationState.value.copy(
                            isError = true ,
                            errorMessage = e.message.toString()
                        )
                    }
                }
            }

            is AuthenticationEvent.SignInWithGoogle -> {
                _state.value.isLoading = true
                viewModelScope.launch {
                    try {
                        val result = withContext(Dispatchers.IO) {
                            App.create(APP_ID).login(
                                Credentials.google(
                                    token = action.tokenId,
                                    type = GoogleAuthType.ID_TOKEN
                                )
                            ).loggedIn
                        }

                        if (result) {
                            _state.value.isAuthenticated = true
                            _state.value.isLoading = false
                        } else {
                            _state.value.isLoading = false
                            Log.e("AuthViewModel", "User cant be logged in through google services")
                        }
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", e.message.toString())
                    }
                }
            }
        }
    }

    fun signOut(){
        _state.value = _state.value.copy(
            isAuthenticated = false,
            isLoading = false
        )
        viewModelScope.launch(Dispatchers.IO) {
            App.create(APP_ID).currentUser?.logOut()
        }
    }

    private fun validateSignInCredentials(
        name: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if(name.isEmpty()){
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = true,
                errorMessage = "name cannot be empty"
            )
            return false
        }
        if(username.isEmpty()){
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = true,
                errorMessage = "email id cannot be empty"
            )
            return false
        }
        if(password.isEmpty() || confirmPassword.isEmpty()){
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = true,
                errorMessage = "password cannot be empty"
            )
            return false
        }

        if(password != confirmPassword){
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = true,
                errorMessage = "passwords must be same"
            )
        }

        return true
    }

    private fun validateLogInCredentials(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = true,
                errorMessage = "username cannot be empty"
            )
            return false
        } else if (password.isEmpty()) {
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = true,
                errorMessage = "password cannot be empty"
            )
            return false
        } else if (password.length < 6) {
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = true,
                errorMessage = "password cannot be shorter than 6 characters"
            )
            return false
        } else {
            _credentialsValidationState.value = _credentialsValidationState.value.copy(
                isError = false,
                errorMessage = ""
            )
            return true
        }
    }
}