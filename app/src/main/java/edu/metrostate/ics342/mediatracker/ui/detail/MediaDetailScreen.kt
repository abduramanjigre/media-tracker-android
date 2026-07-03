package edu.metrostate.ics342.mediatracker.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.Media
import edu.metrostate.ics342.mediatracker.data.model.Review
import edu.metrostate.ics342.mediatracker.data.model.creatorCredit

import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDetailScreen(
    mediaId: Int,
    onNavigateBack: () -> Unit,
    onWriteReview: (Int) -> Unit
) {
    val context = LocalContext.current

    val media = remember(mediaId) {
        FakeMediaRepository.mediaList.find { it.id == mediaId } ?: FakeMediaRepository.mediaList.first()
    }

    // Hardcoded reviews
    val fakeReviews = listOf(
        Review(
            userId = "user-002",
            mediaId = media.id,
            rating = 5,
            reviewText = "This was absolutely amazing! Highly recommend it to everyone.",
            createdAt = "2024-01-22T14:30:00Z",
            user = FakeMediaRepository.followers[0]
        ),
        Review(
            userId = "user-003",
            mediaId = media.id,
            rating = 4,
            reviewText = "Pretty good, though the middle part dragged a bit.",
            createdAt = "2024-01-21T11:15:00Z",
            user = FakeMediaRepository.followers[1]
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, stringResource(R.string.action_back))
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.MoreVert, stringResource(R.string.action_more_options))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cover Image
            Card(
                modifier = Modifier
                    .size(width = 160.dp, height = 240.dp)
                    .clip(RoundedCornerShape(8.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                if (media.coverUrl != null) {
                    AsyncImage(
                        model = media.coverUrl,
                        contentDescription = media.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (media.mediaType) {
                                "book" -> "📖"; "movie" -> "🎬"; "show" -> "📺"
                                else -> "?"
                            },
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Title and Creator
            Text(
                text = media.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = media.creatorCredit(context),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            // Rating row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB400),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                val locale = LocalConfiguration.current.locales[0]
                Text(
                    text = String.format(locale, "%.1f", media.averageRating),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "(${media.ratingCount})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(24.dp))

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.status_want_to))
                }
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save")
                }
            }

            Spacer(Modifier.height(32.dp))

            // About section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "This is a placeholder description for the media item. In the future, this will be populated with real data from the API including a full synopsis of the book, movie, or show.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(32.dp))

            // Stat Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBox(label = "Year", value = media.publishedYear?.toString() ?: "N/A")
                
                val middleLabel = when (media.mediaType) {
                    "book" -> "Pages"
                    "movie" -> "Runtime"
                    "show" -> "Seasons"
                    else -> "Details"
                }
                val middleValue = when (media.mediaType) {
                    "book" -> "320"
                    "movie" -> "124m"
                    "show" -> "3"
                    else -> "N/A"
                }
                StatBox(label = middleLabel, value = middleValue)
                
                StatBox(label = "Genre", value = media.genres.firstOrNull() ?: "N/A")
            }

            Spacer(Modifier.height(32.dp))

            // Reviews section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reviews (${fakeReviews.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { onWriteReview(media.id) }) {
                    Text("+ Write Review")
                }
            }

            Spacer(Modifier.height(16.dp))

            fakeReviews.forEach { review ->
                ReviewCard(review)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar placeholder
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.user?.displayName?.firstOrNull()?.toString() ?: "?",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = review.user?.displayName ?: "Unknown User",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Jan 22, 2024", // Placeholder date formatting
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < review.rating) Color(0xFFFFB400) else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            if (!review.reviewText.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = review.reviewText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MediaDetailPreview() {
    MaterialTheme {
        MediaDetailScreen(mediaId = 1, onNavigateBack = {}, onWriteReview = {})
    }
}
