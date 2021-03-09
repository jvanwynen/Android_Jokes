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

                val cat1 = Category( "programming")
                val cat2 = Category( "Misc")
                val cat3 = Category( "Dark")
                val cat4 = Category( "Pun")
                val cat5 = Category( "Spooky")
                val cat6 = Category( "Christmas")

                dao.insertCategory(cat1)
                dao.insertCategory(cat2)
                dao.insertCategory(cat3)
                dao.insertCategory(cat4)
                dao.insertCategory(cat5)
                dao.insertCategory(cat6)


                dao.insert(Joke(jokeText = "I was reading a great book about an immortal dog the other day. It was impossible to put down.", category = cat2.category))
                dao.insert(Joke(jokeText = "A guy walks into a bar and asks for 1.4 root beers.\nThe bartender says \"I'll have to charge you extra, that's a root beer float\".\nThe guy says \"In that case, better make it a double.\"", category = cat1.category))
                dao.insert(Joke(
                    jokeText = "My parents raised me as an only child, which really annoyed my younger brother.",
                    favorite = true, category = cat3.category))
                dao.insert(Joke(jokeText = "There are only 10 kinds of people in this world: those who know binary and those who don't.", category = cat1.category))

            }
        }
    }
}