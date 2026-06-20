package pl.gwsh.movietracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pl.gwsh.movietracker.model.Movie

/** Karta filmu na liscie. */
@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Poster(movie.title, size = 56, posterUrl = movie.posterUrl)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${movie.genre} | ${movie.year}",
                    style = MaterialTheme.typography.bodySmall
                )
                RatingRow(movie.rating)
            }
            // Szybkie przelaczanie statusu - reakcja UI bez przeladowania ekranu
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (movie.watched) Icons.Filled.CheckCircle else Icons.Filled.Add,
                    contentDescription = "Zmien status",
                    tint = if (movie.watched) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

/** Mala karta ze statystyka na ekranie startowym. */
@Composable
fun StatCard(label: String, value: Int, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text("$value", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
fun Poster(title: String, size: Int, posterUrl: String = "") {
    val colors = listOf(
        Color(0xFF6750A4), Color(0xFF386A20), Color(0xFFB3261E),
        Color(0xFF7D5260), Color(0xFF00639B), Color(0xFF855300)
    )
    val bg = colors[(title.firstOrNull()?.code ?: 0) % colors.size]
    val shape = RoundedCornerShape(12.dp)

    if (posterUrl.isNotBlank()) {
        AsyncImage(
            model = posterUrl,
            contentDescription = "Plakat: $title",
            modifier = Modifier
                .size(width = (size * 0.7).dp, height = size.dp)
                .clip(shape)
                .background(bg),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(size.dp)
                .clip(shape)
                .background(bg),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Plakat",
                modifier = Modifier.size((size * 0.5).dp),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}


@Composable
fun RatingRow(rating: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val stars = (rating + 1) / 2   // przeliczenie 0-10 na 0-5 gwiazdek
        repeat(5) { i ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (i < stars) Color(0xFFFFC107)
                else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(Modifier.width(6.dp))
        Text("$rating/10", style = MaterialTheme.typography.labelMedium)
    }
}
