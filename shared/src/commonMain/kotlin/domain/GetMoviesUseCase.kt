package domain

import data.model.Movie
import data.repository.MoviesRepository

class GetTrendingMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return moviesRepository.getTrendingMovies()
    }
}

