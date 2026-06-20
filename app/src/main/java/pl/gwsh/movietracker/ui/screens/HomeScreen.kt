package pl.gwsh.movietracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pl.gwsh.movietracker.navigation.Routes
import pl.gwsh.movietracker.ui.components.RatingRow
import pl.gwsh.movietracker.ui.components.StatCard
import pl.gwsh.movietracker.viewmodel.MovieViewModel

/** Ekran startowy z podsumowaniem kolekcji. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: MovieViewModel, nav: NavHostController) {
    val movies by vm.movies.collectAsState()
    val watched = movies.count { it.watched }
    val toWatch = movies.size - watched
    val topRated = movies.maxByOrNull { it.rating }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Katalog filmow") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Twoja kolekcja",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Karty ze statystykami - aktualizuja sie na zywo (recomposition)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Wszystkie", movies.size, Icons.Filled.List, Modifier.weight(1f))
                StatCard("Obejrzane", watched, Icons.Filled.Check, Modifier.weight(1f))
                StatCard("W planie", toWatch, Icons.Filled.PlayArrow, Modifier.weight(1f))
            }

            topRated?.let { m ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Najwyzej oceniony", style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            m.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        RatingRow(m.rating)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { nav.navigate(Routes.LIST) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.List, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Przegladaj katalog")
            }
        }
    }
}
