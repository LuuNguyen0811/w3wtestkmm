package core.di

import core.network.W3wNetworkDataSource
import core.network.ktor.KtorHttpClient
import core.network.ktor.KtorNetwork
import data.repository.MoviesRepository
import domain.GetTrendingMoviesUseCase
import domain.SearchMoviesUseCase
import domain.GetMovieDetailsUseCase
import org.koin.dsl.module

val networkModule = module {
    single { KtorHttpClient.httpClient }
    single<W3wNetworkDataSource> { KtorNetwork(get()) }
    single { MoviesRepository(get(), get()) }
    single { GetTrendingMoviesUseCase(get()) }
    single { SearchMoviesUseCase(get()) }
    single { GetMovieDetailsUseCase(get()) }
} 