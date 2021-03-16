package com.joost.joke_exercise.localstorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.joost.joke_exercise.models.Category
import com.joost.joke_exercise.models.Joke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Joke::class, Category::class], version = 1)
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
                dao.insertCategory(Category( "Programming"))
                dao.insertCategory(Category( "Misc"))
                dao.insertCategory(Category( "Dark"))
                dao.insertCategory(Category( "Pun"))
                dao.insertCategory(Category( "Spooky"))
                dao.insertCategory(Category( "Christmas"))
            }
        }
    }
}