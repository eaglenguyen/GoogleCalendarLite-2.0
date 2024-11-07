package com.example.calendarcompose.presentation.auth

import android.graphics.drawable.shapes.Shape
import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.LoginScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    click: () -> Unit,
    goBack: () -> Unit,
    forgotPassword: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state
    var showLogoutMessage by remember { mutableStateOf(state.signOut) }


    Scaffold(
        modifier = Modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
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
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (showLogoutMessage) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE8F5E9)) // Light green background color for success message
                            .border(
                                width = 1.dp,
                                color = Color(0xFF4CAF50), // Green border color
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle, // Success check icon
                            contentDescription = null,
                            tint = Color(0xFF4CAF50), // Green color for the icon
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "You have successfully logged out.",
                            color = Color(0xFF4CAF50), // Green text color
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

                Text(
                    text = "Login",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .padding(20.dp)
                )
                OutlinedTextField(
                    value = state.emailQuery,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )
                    },
                    label = { Text("Email") },
                    onValueChange = {
                        viewModel.onEvent(LoginEvent.EmailQueryChange(query = it))
                        emailError =
                            Patterns.EMAIL_ADDRESS.matcher(state.emailQuery).matches().not()
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
                    shape = RoundedCornerShape(40.dp),
                    maxLines = 1,
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.passwordQuery,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    onValueChange = { viewModel.onEvent(LoginEvent.PasswordQueryChange(query = it)) },
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    shape = RoundedCornerShape(40.dp),
                    maxLines = 1,
                    singleLine = true
                )


                Button(
                    modifier = modifier
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "one"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(12.dp),

                    onClick = {
                        if (state.emailQuery.isEmpty()) {
                            emailError = true
                            return@Button
                        } else {
                            viewModel.onLoginResult(state.emailQuery, state.passwordQuery)
                        }
                    }
                ) {
                    Text("Login")
                }



                Text(
                    text = "Forgot password?",
                    fontSize = 14.sp,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .clickable { forgotPassword() }
                )

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


                LaunchedEffect(state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        Toast.makeText(context, "Signed in", Toast.LENGTH_SHORT).show()
                        click()
                        viewModel.resetSignInState()
                    }
                }

                LaunchedEffect(state.signOut) {
                    if (state.signOut) {
                        showLogoutMessage = true
                        delay(10000)
                        showLogoutMessage = false
                    }
                }







            }

        }



}
    )

}