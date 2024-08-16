package com.example.calendarcompose.presentation.auth

import com.example.calendarcompose.presentation.calendar.Events
import com.google.firebase.auth.FirebaseUser

data class SignInState(
    val emailQuery: String = "",
    val passwordQuery: String = "",
    val user: FirebaseUser? = null,
    val eventList: List<Events> = emptyList(),
    val isLoading: Boolean = false,
    val isCreationSuccessful : Boolean = false,
    val isSignInSuccessful : Boolean = false,
    val signInError: String? = null,
    val signOut: Boolean = false,
    val deleted: Boolean = false,
    val changed: Boolean = false
    )
