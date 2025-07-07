package core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.ConstructedBy
import androidx.room.RoomDatabaseConstructor
import core.database.dao.MovieDao
import core.database.entities.MovieEntity
import core.database.entities.MovieDetailsEntity

@Database(
    entities = [MovieEntity::class, MovieDetailsEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(MoviesDatabaseConstructor::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MoviesDatabaseConstructor : RoomDatabaseConstructor<MoviesDatabase> {
    override fun initialize(): MoviesDatabase
} 