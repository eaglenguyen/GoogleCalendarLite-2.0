package com.example.calendarcompose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.calendarcompose.presentation.auth.AuthViewModel
import com.example.calendarcompose.presentation.auth.CreateScreen
import com.example.calendarcompose.presentation.auth.ForgotPasswordScreen
import com.example.calendarcompose.presentation.auth.LoginScreen
import com.example.calendarcompose.presentation.calendar.CalendarScreen
import com.example.calendarcompose.presentation.calendar.EventScreen
import com.example.calendarcompose.presentation.welcome.WelcomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel = hiltViewModel()
) {

    // Checks if user is still signed in
    val startDestination = if (viewModel.state.value.user != null) {
        CalendarScreen
    } else {
        WelcomeScreen
    }

    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = startDestination ) {

            composable<WelcomeScreen> {
                WelcomeScreen(
                    animatedVisibilityScope = this@composable,
                    createAcct = {
                        navController.navigate(CreateScreen)
                    },
                    loginScreen = {
                        navController.navigate(LoginScreen)
                    }
                )
            }






            composable<CreateScreen> {
                CreateScreen(
                    animatedVisibilityScope = this@composable,
                    goBack = {
                    navController.navigateUp()
                }, onClick = {
                    navController.navigate(CalendarScreen)
                },
                    viewModel = viewModel
                )
            }


            composable<ForgetScreen>{
                ForgotPasswordScreen(
                    goBack = {
                        navController.navigateUp()
                    }
                )
            }


            composable<LoginScreen> {
                LoginScreen(
                    animatedVisibilityScope = this@composable,
                    click = {
                        navController.navigate(CalendarScreen)
                    },
                    goBack = {
                        navController.navigateUp()
                    },
                    forgotPassword = {
                        navController.navigate(ForgetScreen)
                    },
                    viewModel = viewModel
                )
            }


            composable<CalendarScreen> { backStackEntry ->
                CalendarScreen(
                    onPress = {
                        navController.navigate(EventScreen(it))
                    }, onSignOut = {
                        navController.navigate(WelcomeScreen)
                    },
                    viewModel = viewModel
                )
            }



            composable<EventScreen> {
                val chosenDate = it.toRoute<EventScreen>()
                EventScreen(today = chosenDate.date,
                    onGoBack = {
                        navController.navigateUp()
                    })
            }
        }
    }

    }
