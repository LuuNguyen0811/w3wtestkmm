package core.di

import core.database.MoviesDatabase
import core.database.getMoviesDatabase
import org.koin.dsl.module

actual val platformDatabaseModule = module {
    single<MoviesDatabase> { getMoviesDatabase() }
} 