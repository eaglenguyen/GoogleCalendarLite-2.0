package com.example.calendarcompose.presentation.calendar

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calendarcompose.EventNotificationService
import com.example.calendarcompose.presentation.auth.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onPress: (Long) -> Unit,
    onSignOut: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()

) {
    val state by viewModel.state
    val currentUserEmail = state.user?.email.toString()


    var showSignOutDialog by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Get today's date in milliseconds
        val today = System.currentTimeMillis()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = today)

        Text(
            text = currentUserEmail,
            modifier = Modifier
                .align(Alignment.End)
                .offset((-35).dp,60.dp)
        )
        DatePicker(
            state = datePickerState,
            modifier = Modifier.padding(16.dp),
        )

        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(100.dp)
        ) {
            Button(
                onClick = {
                    // Trigger the click function and pass the selected date in milliseconds
                    val selectedDateMillis = datePickerState.selectedDateMillis ?: today

                    onPress(selectedDateMillis)
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Add an Event",)
            }


            Button(
                onClick = {
                    showSignOutDialog = true
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Sign out",)
            }
        }






        if (showSignOutDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSignOutDialog = false
                },
                title = {
                    Text(text = "Sign out")
                },
                text = {
                    Text("Are you sure you want to sign out?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSignOutDialog = false
                            viewModel.onLogOut()
                            onSignOut()
                            // Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()

                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showSignOutDialog = false
                        }
                    ) {
                        Text("No")
                    }
                    })
    }


        }
}

