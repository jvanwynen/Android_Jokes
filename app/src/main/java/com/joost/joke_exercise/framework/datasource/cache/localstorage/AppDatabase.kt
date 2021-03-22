package com.joost.joke_exercise.framework.datasource.cache.localstorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.joost.joke_exercise.framework.datasource.cache.models.CategoryEntity
import com.joost.joke_exercise.framework.datasource.cache.models.JokeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [JokeEntity::class, CategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jokeDao(): JokeDAO

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        private val applicationScope : CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().jokeDao()

            applicationScope.launch {
                //Because the API does not allow me to get all categories
                dao.insertCategory(CategoryEntity( "Programming"))
                dao.insertCategory(CategoryEntity( "Misc"))
                dao.insertCategory(CategoryEntity( "Dark"))
                dao.insertCategory(CategoryEntity( "Pun"))
                dao.insertCategory(CategoryEntity( "Spooky"))
                dao.insertCategory(CategoryEntity( "Christmas"))
            }
        }
    }
}