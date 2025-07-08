package core.di

import presentation.viewmodel.MoviesListViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { MoviesListViewModel() }
} 