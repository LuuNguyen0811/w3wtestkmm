package com.example.what3words.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import data.model.Movie
import data.model.MovieDetails
import data.model.Genre
import data.model.ProductionCompany
import data.model.ProductionCountry
import data.model.SpokenLanguage

@Composable
fun MovieDetailsScreen(
    movie: Movie,
    movieDetails: MovieDetails?,
    isLoading: Boolean,
    error: String?,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Back button
        Button(
            onClick = onBackClick,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("← Back")
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            // Use movieDetails if available, otherwise fallback to basic movie info
            val details = movieDetails
            val baseImageUrl = "https://image.tmdb.org/t/p/w500"
            
            // Movie poster
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                if (details?.posterPath != null) {
                    AsyncImage(
                        model = "$baseImageUrl${details.posterPath}",
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(android.R.drawable.ic_menu_report_image),
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Movie title
            Text(
                text = details?.title ?: movie.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            // Original title if different
            if (details?.originalTitle != null && details.originalTitle != details.title) {
                Text(
                    text = "Original: ${details.originalTitle}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Movie details row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Year and runtime
                Column {
                    Text(
                        text = "Year: ${(details?.releaseDate ?: movie.releaseDate).take(4)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
//                    if (details?.runtime != null && details.runtime > 0) {
//                        Text(
//                            text = "Runtime: ${details.runtime} min",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = Color.Gray
//                        )
//                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Rating
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "★",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                    Text(
                        text = String.format("%.1f", details?.voteAverage ?: movie.voteAverage),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    if (details?.voteCount != null) {
                        Text(
                            text = "${details.voteCount} votes",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Overview
            if (details?.overview?.isNotBlank() == true) {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = details.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Tagline
            if (details?.tagline?.isNotBlank() == true) {
                Text(
                    text = "\"${details.tagline}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Genres
            if (details?.genres?.isNotEmpty() == true) {
                Text(
                    text = "Genres",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    items(details.genres) { genre ->
                        Card(
                            modifier = Modifier.padding(end = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = genre.name,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Homepage link
            if (details?.homepage?.isNotBlank() == true) {
                val annotatedString = buildAnnotatedString {
                    append("Homepage: ")
                    pushStringAnnotation(tag = "URL", annotation = details.homepage)
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(details.homepage)
                    }
                    pop()
                }
                
                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle error
                                }
                            }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // IMDB link
            if (details?.imdbId?.isNotBlank() == true) {
                val imdbUrl = "https://www.imdb.com/title/${details.imdbId}"
                val annotatedString = buildAnnotatedString {
                    append("IMDB: ")
                    pushStringAnnotation(tag = "URL", annotation = imdbUrl)
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(imdbUrl)
                    }
                    pop()
                }
                
                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Handle error
                                }
                            }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Production companies
            if (details?.productionCompanies?.isNotEmpty() == true) {
                Text(
                    text = "Production Companies",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                details.productionCompanies.forEach { company ->
                    Text(
                        text = "• ${company.name}${if (company.originCountry.isNotBlank()) " (${company.originCountry})" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Additional info
            if (details != null) {
                Text(
                    text = "Additional Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (details.budget > 0) {
                    Text(
                        text = "Budget: $${String.format("%,d", details.budget)}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                if (details.revenue > 0) {
                    Text(
                        text = "Revenue: $${String.format("%,d", details.revenue)}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                if (details.status.isNotBlank()) {
                    Text(
                        text = "Status: ${details.status}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                if (details.popularity > 0) {
                    Text(
                        text = "Popularity: ${String.format("%.1f", details.popularity)}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsScreenPreview() {
    val mockMovie = Movie(
        id = 1,
        title = "Inception",
        releaseDate = "2010-07-16",
        voteAverage = 8.8,
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg"
    )
    
    val mockDetails = MovieDetails(
        id = 1,
        title = "Inception",
        overview = "Dom Cobb is a skilled thief, the absolute best in the dangerous art of extraction...",
        releaseDate = "2010-07-16",
        voteAverage = 8.8,
        voteCount = 12000,
        popularity = 85.5,
        runtime = 148,
        tagline = "Your mind is the scene of the crime",
        homepage = "https://www.inception-movie.com",
        genres = listOf(
            Genre(28, "Action"),
            Genre(878, "Science Fiction"),
            Genre(53, "Thriller")
        )
    )
    
    MovieDetailsScreen(
        movie = mockMovie,
        movieDetails = mockDetails,
        isLoading = false,
        error = null,
        onBackClick = {}
    )
} 