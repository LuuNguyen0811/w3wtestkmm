package core.di

import core.presentation.MoviesListViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { MoviesListViewModel() }
} 