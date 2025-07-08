package com.example.what3words.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import presentation.viewmodel.MoviesListViewModel
import data.model.Movie

@Composable
fun MoviesListScreen() {
    val viewModel = remember { MoviesListViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.showMovieDetails && uiState.selectedMovie != null) {
        MovieDetailsScreen(
            movie = uiState.selectedMovie!!,
            movieDetails = uiState.movieDetails,
            isLoading = uiState.isLoadingDetails,
            error = uiState.detailsError,
            onBackClick = { viewModel.onBackFromMovieDetails() }
        )
        return
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.searchMovies(it) },
            label = { Text("Search movies...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = uiState.listHeader,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(uiState.error!!, color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            uiState.movies.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (uiState.isSearchMode) "No search results found" else "No trending movies found"
                    )
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(uiState.movies) { movie ->
                        MovieListItem(
                            movie = movie,
                            onMovieClick = { viewModel.onMovieClick(movie) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieListItem(movie: Movie, onMovieClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onMovieClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Movie image (poster or backdrop)
        Box(
            modifier = Modifier
                .size(80.dp, 120.dp)
                .padding(end = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            val imageUrl = movie.posterPath?.let { "https://api.themoviedb.org/3/trending/movie$it" }
            AsyncImage(
                model = imageUrl,
                contentDescription = "Movie Poster"
            )
        }
        
        // Movie details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.releaseDate.take(4), // Year only
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "★",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
            Text(
                text = String.format("%.1f", movie.voteAverage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesListScreenPreview() {
    MoviesListScreen()
}