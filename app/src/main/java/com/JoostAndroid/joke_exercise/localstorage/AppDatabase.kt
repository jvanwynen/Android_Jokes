package com.JoostAndroid.joke_exercise.localstorage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.JoostAndroid.joke_exercise.models.Joke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Joke::class], version = 1)
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
                dao.insert(Joke( jokeText = "I was reading a great book about an immortal dog the other day. It was impossible to put down.", favorite = true))
                dao.insert(Joke( jokeText = "A guy walks into a bar and asks for 1.4 root beers.\nThe bartender says \"I'll have to charge you extra, that's a root beer float\".\nThe guy says \"In that case, better make it a double.\""))
                dao.insert(Joke( jokeText = "My parents raised me as an only child, which really annoyed my younger brother."))
                dao.insert(Joke( jokeText = "There are only 10 kinds of people in this world: those who know binary and those who don't."))

            }

        }

    }


}