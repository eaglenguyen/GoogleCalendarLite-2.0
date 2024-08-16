package com.example.calendarcompose.di

import com.example.calendarcompose.repository.RepoAuth
import com.example.calendarcompose.repository.RepoAuthImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(impl: RepoAuthImpl): RepoAuth = impl

    @Provides
    fun provideFirestore() = FirebaseFirestore.getInstance()





}