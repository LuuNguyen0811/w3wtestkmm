package core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MoviesDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("movies_database.db")
    
    return Room.databaseBuilder<MoviesDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}

fun getMoviesDatabase(context: Context): MoviesDatabase {
    return getDatabaseBuilder(context)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
} 