package pl.gwsh.movietracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pl.gwsh.movietracker.model.FilterStatus
import pl.gwsh.movietracker.navigation.Routes
import pl.gwsh.movietracker.ui.components.MovieCard
import pl.gwsh.movietracker.viewmodel.MovieViewModel

/** Ekran listy z wyszukiwaniem i filtrowaniem (dane dynamiczne). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(vm: MovieViewModel, nav: NavHostController) {
    val movies by vm.movies.collectAsState()
    val filter by vm.filter.collectAsState()
    val query by vm.query.collectAsState()

    val visible = vm.visibleMovies(movies, filter, query)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Katalog (${visible.size})") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate("${Routes.FORM}/0") }) {
                Icon(Icons.Filled.Add, contentDescription = "Dodaj film")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {

            // Pole wyszukiwania - TextField
            OutlinedTextField(
                value = query,
                onValueChange = { vm.setQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("Szukaj po tytule lub gatunku") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true
            )

            // Filtry statusu - FilterChip
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterStatus.values().forEach { status ->
                    FilterChip(
                        selected = filter == status,
                        onClick = { vm.setFilter(status) },
                        label = { Text(status.label) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (visible.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Brak filmow dla wybranych kryteriow.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(visible, key = { it.id }) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { nav.navigate("${Routes.DETAIL}/${movie.id}") },
                            onToggle = { vm.toggleWatched(movie.id) }
                        )
                    }
                }
            }
        }
    }
}
