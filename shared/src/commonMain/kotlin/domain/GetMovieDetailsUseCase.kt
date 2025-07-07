package domain

import data.model.MovieDetails
import data.repository.MoviesRepository

class GetMovieDetailsUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int): MovieDetails? {
        return moviesRepository.getMovieDetails(movieId)
    }
} 