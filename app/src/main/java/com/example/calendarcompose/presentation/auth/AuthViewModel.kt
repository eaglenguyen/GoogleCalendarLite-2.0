package com.example.calendarcompose.presentation.auth

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarcompose.presentation.calendar.Events
import com.example.calendarcompose.repository.RepoAuth
import com.example.calendarcompose.utli.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: RepoAuth
): ViewModel() {
    //    var state by mutableStateOf(SignInState()) <-- non observable , failed to recompose UI after change





    private val _state = mutableStateOf(SignInState())
    val state: State<SignInState> get() = _state


    private val currentUser: FirebaseUser?
        get() = repository.currentUser


    init {
        currentUser?.let { user ->
            _state.value = _state.value.copy(user = user)
        }
    }




    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailQueryChange -> {
                _state.value = _state.value.copy(emailQuery = event.query)
            }
            is LoginEvent.PasswordQueryChange -> {
                _state.value = _state.value.copy(passwordQuery = event.query)
            }
        }
    }





    fun onLoginResult (email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email,password).collect { result ->
            when(result) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        signInError = result.message,
                        isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = result.isLoading
                    )
                }
                is Resource.Success -> {
                    result.data.let { user ->
                        _state.value = _state.value.copy(
                            user = user,
                            isSignInSuccessful = true,
                            signOut = false,
                            isLoading = false
                        )

                    }
                }
            }

        }
    }

    fun resetSignInState() {
        _state.value = _state.value.copy(isSignInSuccessful = false)
    }


    fun createUser (email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email,password).collect{ result ->
            when(result) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        signInError = result.message,
                        isLoading = false

                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = result.isLoading
                    )
                }
                is Resource.Success -> {
                    result.data.let { user ->
                        _state.value = _state.value.copy(
                            user = user,
                            isCreationSuccessful = true,
                            isLoading = false

                        )

                    }
                }
            }

        }
    }


    fun onLogOut() = viewModelScope.launch {
        repository.logOut().collect { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        signOut = true,
                        user = null
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        signInError = result.message
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete (userId: String, events: Events) = viewModelScope.launch {
        repository.delete(userId, events).collect{ result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(deleted = true)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        signInError = result.message
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }

            }
        }
    }

    fun edit (userId: String, newEvents: String, events: Events) = viewModelScope.launch {
        repository.edit(userId, newEvents, events).collect{ result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(changed = true)
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        signInError = result.message
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }

            }
        }
    }



    fun fetchData(userId: String, date: String) = viewModelScope.launch {
        repository.fetchData(userId,date).collect{ result ->
            when(result) {
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                    signInError = result.message,
                    isLoading = false
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = false)
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        eventList = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
            }
        }
        }

    fun resetPassword(email: String) = viewModelScope.launch {
        repository.resetPassword(email).collect { result ->
            when(result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        user = null
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        signInError = result.message
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }
            }
        }
    }
    }