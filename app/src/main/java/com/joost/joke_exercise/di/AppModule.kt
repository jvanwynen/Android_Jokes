package com.joost.joke_exercise.di

import android.app.Application
import androidx.room.Room
import com.joost.joke_exercise.framework.datasource.cache.abstraction.JokeDaoService
import com.joost.joke_exercise.framework.datasource.cache.implementation.JokeDaoServiceImpl
import com.joost.joke_exercise.framework.datasource.cache.localstorage.AppDatabase
import com.joost.joke_exercise.framework.datasource.cache.localstorage.JokeDAO
import com.joost.joke_exercise.framework.datasource.cache.mapper.CacheCategoryMapper
import com.joost.joke_exercise.framework.datasource.cache.mapper.CacheJokeMapper
import com.joost.joke_exercise.framework.datasource.network.repository.JokeRepository
import com.joost.joke_exercise.framework.datasource.network.repository.JokeRepositoryImpl
import com.joost.joke_exercise.framework.datasource.network.remote.JokeService
import com.joost.joke_exercise.framework.datasource.network.abstraction.JokeRemoteService
import com.joost.joke_exercise.framework.datasource.network.implementation.JokeRemoteServiceImpl
import com.joost.joke_exercise.framework.datasource.network.mapper.NetworkJokeMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
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
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(JokeService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideJokeApi(retrofit: Retrofit): JokeService =
        retrofit.create(JokeService::class.java)

    @Provides
    @Singleton
    fun provideJokeRepository(
        jokeDaoService: JokeDaoService,
        jokeRemoteService: JokeRemoteService
    ): JokeRepository {
        return JokeRepositoryImpl(jokeRemoteService, jokeDaoService)
    }


    @Singleton
    @Provides
    fun provideJokeCacheMapper(): CacheJokeMapper {
        return CacheJokeMapper()
    }

    @Singleton
    @Provides
    fun provideCategoryCacheMapper(): CacheCategoryMapper {
        return CacheCategoryMapper()
    }

    @Singleton
    @Provides
    fun provideJokeDaoService(
        cacheCategoryMapper: CacheCategoryMapper,
        cacheJokeMapper: CacheJokeMapper,
        jokeDAO: JokeDAO
    ): JokeDaoService {
        return JokeDaoServiceImpl(jokeDAO, cacheJokeMapper, cacheCategoryMapper)
    }

    @Singleton
    @Provides
    fun provideJokeRemoteService(
        jokeService: JokeService,
        networkJokeMapper: NetworkJokeMapper
    ): JokeRemoteService {
        return JokeRemoteServiceImpl(jokeService, networkJokeMapper)
    }

    @Singleton
    @Provides
    fun provideNetworkJokeMapper(): NetworkJokeMapper {
        return NetworkJokeMapper()
    }


}