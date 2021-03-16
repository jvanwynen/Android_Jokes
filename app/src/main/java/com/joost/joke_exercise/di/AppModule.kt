package com.joost.joke_exercise.di

import android.app.Application
import android.view.View
import androidx.room.Room
import com.joost.joke_exercise.api.JokeApi
import com.joost.joke_exercise.localstorage.AppDatabase
import com.joost.joke_exercise.models.Joke
import com.joost.joke_exercise.ui.addEdit.AddEditJokeFragment
import com.joost.joke_exercise.ui.home.JokeAdapter
import com.joost.joke_exercise.ui.home.JokesFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app : Application,
        callback: AppDatabase.Callback
    ) = Room.databaseBuilder(app, AppDatabase::class.java, "AppDatabase")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideJokeDao(db: AppDatabase) = db.jokeDao()

    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(JokeApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideJokeApi(retrofit: Retrofit): JokeApi =
        retrofit.create(JokeApi::class.java)
}