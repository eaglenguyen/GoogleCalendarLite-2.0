package com.example.calendarcompose.presentation.calendar

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseClient(
    private val context: Context
) {

    private val Firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun saveEvent(events: Events, userId: String) {
        Firestore.collection(userId)
            .add(events)
            .addOnSuccessListener {
                Log.d(TAG, "Added")
            }
            .addOnFailureListener{
                Log.w(TAG, "Error")
            }
    }


}