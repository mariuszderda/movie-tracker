package pl.gwsh.movietracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pl.gwsh.movietracker.navigation.Routes
import pl.gwsh.movietracker.ui.components.Poster
import pl.gwsh.movietracker.ui.components.RatingRow
import pl.gwsh.movietracker.viewmodel.MovieViewModel

/** Ekran szczegolow wybranego filmu. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(vm: MovieViewModel, nav: NavHostController, id: Int) {
    val movies by vm.movies.collectAsState()
    val movie = movies.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegoly") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Wstecz")
                    }
                },
                actions = {
                    if (movie != null) {
                        IconButton(onClick = { nav.navigate("${Routes.FORM}/${movie.id}") }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edytuj")
                        }
                        IconButton(onClick = {
                            vm.delete(movie.id)
                            nav.popBackStack()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Usun")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (movie == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nie znaleziono filmu.")
            }
            return@Scaffold
        }

        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Poster(movie.title, size = 96, posterUrl = movie.posterUrl)
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("${movie.genre} | ${movie.year}", style = MaterialTheme.typography.bodyMedium)
                    RatingRow(movie.rating)
                    AssistChip(
                        onClick = { vm.toggleWatched(movie.id) },
                        label = { Text(if (movie.watched) "Obejrzany" else "Do obejrzenia") },
                        leadingIcon = {
                            Icon(
                                if (movie.watched) Icons.Filled.Check else Icons.Filled.PlayArrow,
                                contentDescription = null
                            )
                        }
                    )
                }
            }

            HorizontalDivider()

            Text("Notatka", style = MaterialTheme.typography.titleMedium)
            Text(
                movie.note.ifBlank { "Brak notatki." },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { nav.navigate("${Routes.FORM}/${movie.id}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Edytuj film")
            }
        }
    }
}
