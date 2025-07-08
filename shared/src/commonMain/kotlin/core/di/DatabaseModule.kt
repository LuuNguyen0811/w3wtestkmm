package core.di

import core.database.MoviesDatabase
import org.koin.dsl.module

val databaseModule = module {
    includes(platformDatabaseModule)
}

expect val platformDatabaseModule: org.koin.core.module.Module