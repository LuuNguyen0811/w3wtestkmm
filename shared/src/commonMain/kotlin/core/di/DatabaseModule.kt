package core.di

import core.database.MoviesDatabase
import org.koin.dsl.module

val databaseModule = module {
    // Platform-specific database instances will be provided in platform modules
    includes(platformDatabaseModule)
}

// Platform-specific database module
expect val platformDatabaseModule: org.koin.core.module.Module 