package com.example.calendarcompose.presentation.auth

sealed class LoginEvent {
    data class EmailQueryChange(val query: String): LoginEvent()
    data class PasswordQueryChange(val query: String): LoginEvent()
}