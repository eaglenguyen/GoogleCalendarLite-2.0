package com.example.calendarcompose.navigation

import kotlinx.serialization.Serializable

@Serializable
object WelcomeScreen

@Serializable
object LoginScreen

@Serializable
object CreateScreen

@Serializable
object ForgetScreen


@Serializable
object CalendarScreen

@Serializable
data class EventScreen (
    val date: Long
)