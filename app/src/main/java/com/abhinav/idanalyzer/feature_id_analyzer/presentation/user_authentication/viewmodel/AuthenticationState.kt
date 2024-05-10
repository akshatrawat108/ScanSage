package com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.viewmodel

data class AuthenticationState(
    var isAuthenticated : Boolean = false,
    var isLoading : Boolean = false
)

data class CredentialsValidate(
    var isError: Boolean = false,
    var errorMessage: String = ""
)