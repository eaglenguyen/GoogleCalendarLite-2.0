package com.example.calendarcompose.presentation.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.CreateScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    goBack: () -> Unit,
    onClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state




    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { goBack() } ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(

                )
            )
        },
        content = { paddingValues ->


        var emailError by remember { mutableStateOf(false) }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Register",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .padding(20.dp)

                )
                OutlinedTextField(
                    value = state.emailQuery,
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null ) },
                    onValueChange = {
                        viewModel.onEvent(LoginEvent.EmailQueryChange(query = it))
                        emailError = Patterns.EMAIL_ADDRESS.matcher(state.emailQuery).matches().not()
                    },
                    supportingText = {
                        if (emailError) {
                            Text(text = "Enter a valid email address")
                        }
                    },
                    isError = emailError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    placeholder =  { Text (text = "Email...") },
                    maxLines = 1,
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.passwordQuery,
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                    onValueChange = { viewModel.onEvent(LoginEvent.PasswordQueryChange(query = it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 10.dp),
                    placeholder =  { Text (text = "Password...") },
                    maxLines = 1,
                    singleLine = true

                )


                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 80.dp, vertical = 10.dp)
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "two"),
                                animatedVisibilityScope = animatedVisibilityScope
                        ),
                        shape = RoundedCornerShape(12.dp),

                        onClick = {
                        if (state.emailQuery.isEmpty()) {
                            emailError = true
                            return@Button
                        } else {
                            viewModel.createUser(state.emailQuery,state.passwordQuery)
                        }
                    }
                    ) {
                        Text("Sign Up")
                    }

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }


                LaunchedEffect(key1 = state.signInError) {
                    state.signInError?.let { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                LaunchedEffect(state.isCreationSuccessful) {
                        if (state.isCreationSuccessful) {
                            Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
                            onClick()
                        }
                    }
                }



        }



} ) }