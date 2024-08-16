package com.example.calendarcompose.repository

import com.example.calendarcompose.presentation.calendar.Events
import com.example.calendarcompose.utli.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RepoAuthImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
): RepoAuth {


    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun loginUser(email: String, password: String): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            delay(2000)
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                emit(Resource.Success(result.user!!))
            }
            catch (e:Exception) {
                emit(Resource.Error(e.message ?: "An unexpected error occurred"))
            }

        }
    }

    override suspend fun registerUser(email: String, password: String): Flow<Resource<FirebaseUser>> {
        return flow {
            emit(Resource.Loading())
            delay(2000)
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
                emit(Resource.Success(result.user!!))
            }
            catch (e:Exception) {
                emit(Resource.Error(e.message ?: "An unexpected error occurred"))
            }
        }
    }

    override suspend fun logOut(): Flow<Resource<Unit>> {
        return flow {
            try {
                firebaseAuth.signOut()
                emit(Resource.Success(Unit))
            }
            catch (e:Exception) {
                emit(Resource.Error(e.message ?: "An unexpected error occurred"))
            }
        }
    }

    override suspend fun fetchData(userId: String, date: String): Flow<Resource<List<Events>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val snapshot = firestore.collection(userId)
                    .whereEqualTo("date", date)
                    .get()
                    .await()

                val eventList = snapshot.documents.map {  document ->
                    val data = document.data!!
                    Events(
                        data["event"].toString(),
                        data["date"].toString(),
                        data["timeStamp"].toString()
                    )
                }
                emit(Resource.Success(eventList))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "An error occurred"))
            }
        }
    }

    override suspend fun delete(userId: String, events: Events): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                val snapshot = firestore.collection(userId)
                    .whereEqualTo("event", events.event)
                    .get()
                    .await()

                for (document in snapshot) {
                    document.reference.delete().await()
                    //firestore.collection(userId).document(document.id).delete().await()
            }
                emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: " An error occurred"))
            }
        }
    }

    override suspend fun edit(userId: String, newEvent: String, events: Events): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                val snapshot = firestore.collection(userId)
                    .whereEqualTo("event", events.event)
                    .get()
                    .await()

                for (document in snapshot.documents) {
                    document.reference.update("event", newEvent).await()
                }
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: " An error occurred"))
            }
        }
    }

    override suspend fun resetPassword(email: String): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                firebaseAuth.sendPasswordResetEmail(email)
                emit(Resource.Success(Unit))
            } catch (e:Exception) {
                emit(Resource.Error(e.message ?: "An unexpected error occurred"))
            }
        }
    }


}