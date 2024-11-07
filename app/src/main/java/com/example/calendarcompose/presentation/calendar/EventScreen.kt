package com.example.calendarcompose.presentation.calendar

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calendarcompose.EventNotificationService
import com.example.calendarcompose.presentation.auth.AuthViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    today: Long,
    onGoBack:() -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {

    //val eventList by viewModel.itemList

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val fireBaseClient by lazy {
        FirebaseClient(
            context = context
        )
    }

    val eventNotificationService by lazy {
        EventNotificationService(
            contexts = context
        )
    }

    var eventText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedEvent by remember { mutableStateOf<Events?>(null) }
    var inputText by remember { mutableStateOf("") }


    // var showSavedDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val state by viewModel.state
    val currentUserId = state.user?.uid.toString()


    // Date
    val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).apply {
        // get right timezone for correct day
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val formattedDate = dateFormat.format(Date(today))

    // Time
    val timeState = rememberTimePickerState()
    // var showTimePicker by remember { mutableStateOf(false) }
    val hour = timeState.hour
    val min = timeState.minute
    val timeStamp = String.format("%s:%02d", hour, min)

    val calendar = Calendar.getInstance().apply {
        // 86400000 ms = 24 hours
        timeInMillis = today + 86400000
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, min)
    }

    Log.d("EventScreen", "Notification time in millis: ${calendar.timeInMillis}")



    val event = Events(eventText,formattedDate,timeStamp)

    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.fetchData(currentUserId, formattedDate)


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { onGoBack() }) {
                    Icon(Icons.Default.Close , contentDescription = "Close" )
                }

                Button(onClick = {
                    if (eventText.isEmpty()) {
                        Toast.makeText(context, "Please enter a task!", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    // Copy the eventText value before clearing it
                    val savedEventTxt = eventText
                    eventNotificationService.showNotification(context,calendar.timeInMillis,savedEventTxt)

                    coroutineScope.launch {
                        fireBaseClient.saveEvent(event, currentUserId)
                        snackbarHostState.showSnackbar("Saved \"$savedEventTxt\" on $formattedDate")
                    }
                    // Clear the eventText immediately
                    eventText = ""
                    focusManager.clearFocus()


                }) {
                    Text("Add")
                }
            }



            OutlinedTextField(
                value = eventText,
                onValueChange = { eventText = it },
                label = { Text("Enter Event") },
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "clear text",
                        modifier = Modifier.clickable { eventText = "" })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Text(
                text = formattedDate ,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)) {
                TimePicker(state = timeState)
            }



            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                items(state.eventList) { item ->
                    EventItem(
                        event = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)

                            .clickable {
                                selectedEvent = item
                                showDialog = true
                            }
                    )
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        inputText = ""
                        focusManager.clearFocus()
                        showDialog = false
                                       },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Edit Task")
                            IconButton(onClick = {
                                selectedEvent?.let { viewModel.delete(currentUserId, it) }
                                focusManager.clearFocus()
                                showDialog = false
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Deleted")

                                }
                            } ) {
                                Icon(Icons.Default.Delete , contentDescription = "Delete" )
                            }
                        }

                            },
                    text = {
                        Column {
                            Text(text = "")
                            TextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                label = { Text("Enter new event:") }

                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val savedInputText = inputText

                            if (savedInputText.isEmpty()) {
                                Toast.makeText(context, "Please enter a task!", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            }

                            selectedEvent?.let { viewModel.edit(currentUserId,savedInputText,it) }
                            inputText = ""
                            focusManager.clearFocus()
                            showDialog = false

                        }) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            inputText = ""
                            focusManager.clearFocus()
                            showDialog = false
                        }) {
                            Text("Cancel")
                        }
                    },
                )
            }
        }

    }



}
