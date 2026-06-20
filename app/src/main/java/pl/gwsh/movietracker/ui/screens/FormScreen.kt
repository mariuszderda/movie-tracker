package pl.gwsh.movietracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import pl.gwsh.movietracker.model.Movie
import pl.gwsh.movietracker.viewmodel.MovieViewModel

/** Ekran formularza: dodawanie nowego lub edycja istniejacego filmu. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(vm: MovieViewModel, nav: NavHostController, id: Int) {
    val existing = remember(id) { vm.getMovie(id) }

    // rememberSaveable - stan formularza przezywa obrot ekranu
    var title by rememberSaveable { mutableStateOf(existing?.title ?: "") }
    var genre by rememberSaveable { mutableStateOf(existing?.genre ?: "") }
    var year by rememberSaveable { mutableStateOf(existing?.year ?: "") }
    var rating by rememberSaveable { mutableStateOf(existing?.rating ?: 5) }
    var watched by rememberSaveable { mutableStateOf(existing?.watched ?: false) }
    var note by rememberSaveable { mutableStateOf(existing?.note ?: "") }
    var posterUrl by rememberSaveable { mutableStateOf(existing?.posterUrl ?: "") }

    val isNew = existing == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNew) "Nowy film" else "Edycja") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Wstecz")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Tytul") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("Gatunek") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = year,
                onValueChange = { year = it.filter { c -> c.isDigit() } },
                label = { Text("Rok produkcji") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // URL plakatu - wklej bezposredni link do obrazka (.jpg / .png)
            OutlinedTextField(
                value = posterUrl,
                onValueChange = { posterUrl = it.trim() },
                label = { Text("URL plakatu (link do obrazka)") },
                placeholder = { Text("https://...jpg") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Podglad plakatu na zywo
            if (posterUrl.isNotBlank()) {
                AsyncImage(
                    model = posterUrl,
                    contentDescription = "Podglad plakatu",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(width = 110.dp, height = 160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Ocena - suwak (reakcja UI na zywo)
            Text("Ocena: $rating / 10", style = MaterialTheme.typography.titleMedium)
            Slider(
                value = rating.toFloat(),
                onValueChange = { rating = it.toInt() },
                valueRange = 0f..10f,
                steps = 9
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Obejrzany", modifier = Modifier.weight(1f))
                Switch(checked = watched, onCheckedChange = { watched = it })
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Notatka") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    vm.save(
                        Movie(
                            id = existing?.id ?: 0,
                            title = title.ifBlank { "Bez tytulu" },
                            genre = genre.ifBlank { "Inny" },
                            year = year.ifBlank { "—" },
                            rating = rating,
                            watched = watched,
                            note = note,
                            posterUrl = posterUrl
                        )
                    )
                    nav.popBackStack()
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (isNew) "Dodaj film" else "Zapisz zmiany")
            }
        }
    }
}