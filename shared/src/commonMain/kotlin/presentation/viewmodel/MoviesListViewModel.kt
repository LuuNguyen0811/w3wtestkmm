package presentation.viewmodel

import core.utils.CoroutineViewModel
import data.model.Movie
import data.model.MovieDetails
import domain.GetTrendingMoviesUseCase
import domain.SearchMoviesUseCase
import domain.GetMovieDetailsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import presentation.uimodel.MoviesUiState

class MoviesListViewModel() : CoroutineViewModel(), KoinComponent {
    private val getTrendingMoviesUseCase: GetTrendingMoviesUseCase by inject()
    private val searchMoviesUseCase: SearchMoviesUseCase by inject()
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase by inject()
    
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()
    
    private val scope = CoroutineScope(Dispatchers.IO)
    private var searchJob: Job? = null

    init {
        loadTrendingMovies()
    }

    fun loadTrendingMovies() {
        scope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                isSearchMode = false,
                listHeader = "Trending movies",
                showMovieDetails = false,
                selectedMovie = null
            )
            try {
                val movies = getTrendingMoviesUseCase()
                _uiState.value = _uiState.value.copy(
                    movies = movies,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load trending movies"
                )
            }
        }
    }

    fun searchMovies(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        
        searchJob?.cancel()
        
        if (query.isBlank()) {
            loadTrendingMovies()
            return
        }
        
        searchJob = scope.launch {
            delay(300)
            
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                isSearchMode = true,
                listHeader = "Search results",
                showMovieDetails = false,
                selectedMovie = null
            )
            try {
                val movies = searchMoviesUseCase(query)
                _uiState.value = _uiState.value.copy(
                    movies = movies,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Search failed"
                )
            }
        }
    }

    fun retry() {
        if (_uiState.value.isSearchMode) {
            searchMovies(_uiState.value.searchQuery)
        } else {
            loadTrendingMovies()
        }
    }

    fun onMovieClick(movie: Movie) {
        _uiState.value = _uiState.value.copy(
            selectedMovie = movie,
            showMovieDetails = true,
            movieDetails = null,
            isLoadingDetails = true,
            detailsError = null
        )
        
        scope.launch {
            try {
                val movieDetails = getMovieDetailsUseCase(movie.id)
                _uiState.value = _uiState.value.copy(
                    movieDetails = movieDetails,
                    isLoadingDetails = false,
                    detailsError = if (movieDetails == null) "Failed to load movie details" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingDetails = false,
                    detailsError = e.message ?: "Failed to load movie details"
                )
            }
        }
    }

    fun onBackFromMovieDetails() {
        _uiState.value = _uiState.value.copy(
            showMovieDetails = false,
            selectedMovie = null,
            movieDetails = null,
            isLoadingDetails = false,
            detailsError = null
        )
    }

    // For IOS
    fun observeMovies(onChange: (MoviesUiState) -> Unit) {
        uiState.onEach {
            onChange(it)
        }.launchIn(coroutineScope)
    }
} 