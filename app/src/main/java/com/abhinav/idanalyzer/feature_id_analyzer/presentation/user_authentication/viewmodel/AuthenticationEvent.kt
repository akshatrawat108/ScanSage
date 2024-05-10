package com.abhinav.idanalyzer.feature_id_analyzer.presentation.user_authentication.viewmodel

sealed class AuthenticationEvent {
    data class LogInWithEmail(val username: String, val password: String) : AuthenticationEvent()
    data class SignInWithEmail(val name: String,
                               val email: String,
                               val password: String,
                               val confirmPassword: String) : AuthenticationEvent()
    data class SignInWithGoogle(val tokenId: String) : AuthenticationEvent()
}