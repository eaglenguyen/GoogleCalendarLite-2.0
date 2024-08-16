package com.example.calendarcompose.repository

import com.example.calendarcompose.presentation.calendar.Events
import com.example.calendarcompose.utli.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface RepoAuth {
    val currentUser: FirebaseUser?
    suspend fun loginUser(email: String, password: String): Flow<Resource<FirebaseUser>>
    suspend fun registerUser(email: String,password: String): Flow<Resource<FirebaseUser>>
    suspend fun logOut(): Flow<Resource<Unit>>
    suspend fun fetchData(userId: String,date: String): Flow<Resource<List<Events>>>
    suspend fun delete(userId: String, events: Events): Flow<Resource<Unit>>
    suspend fun edit(userId: String, newEvent: String, events: Events): Flow<Resource<Unit>>
    suspend fun resetPassword(email: String): Flow<Resource<Unit>>
}