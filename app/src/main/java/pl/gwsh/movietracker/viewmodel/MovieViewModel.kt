package pl.gwsh.movietracker.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.gwsh.movietracker.model.FilterStatus
import pl.gwsh.movietracker.model.Movie

/** Przechowuje i modyfikuje stan aplikacji (lista filmow, filtr, wyszukiwanie). */
class MovieViewModel : ViewModel() {

    private val _movies = MutableStateFlow(sampleMovies())
    val movies = _movies.asStateFlow()

    private val _filter = MutableStateFlow(FilterStatus.ALL)
    val filter = _filter.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private var nextId = 100

    fun setFilter(f: FilterStatus) { _filter.value = f }

    fun setQuery(q: String) { _query.value = q }

    fun getMovie(id: Int): Movie? = _movies.value.find { it.id == id }

    fun toggleWatched(id: Int) {
        _movies.value = _movies.value.map {
            if (it.id == id) it.copy(watched = !it.watched) else it
        }
    }

    fun delete(id: Int) {
        _movies.value = _movies.value.filterNot { it.id == id }
    }

    /** Dodaje nowy film (id == 0) albo aktualizuje istniejacy. */
    fun save(movie: Movie) {
        _movies.value = if (movie.id == 0) {
            _movies.value + movie.copy(id = nextId++)
        } else {
            _movies.value.map { if (it.id == movie.id) movie else it }
        }
    }

    /** Lista po zastosowaniu filtra statusu i wyszukiwania. */
    fun visibleMovies(all: List<Movie>, f: FilterStatus, q: String): List<Movie> {
        return all.filter { m ->
            val matchStatus = when (f) {
                FilterStatus.ALL -> true
                FilterStatus.TO_WATCH -> !m.watched
                FilterStatus.WATCHED -> m.watched
            }
            val matchQuery = q.isBlank() ||
                m.title.contains(q, ignoreCase = true) ||
                m.genre.contains(q, ignoreCase = true)
            matchStatus && matchQuery
        }
    }

    private fun sampleMovies() = listOf(
        Movie(1, "Incepcja", "Sci-Fi", "2010", 9, true, "Genialna konstrukcja snu w snie.", "https://fwcdn.pl/fpo/08/91/500891/7354571_1.10.webp"),
        Movie(2, "Diuna: Czesc druga", "Sci-Fi", "2024", 8, false, "Do obejrzenia w weekend.", "https://fwcdn.pl/fpo/34/81/10003481/8151312.10.webp"),
        Movie(3, "Skazani na Shawshank", "Dramat", "1994", 10, true, "Klasyk, ktory zawsze wraca.", "https://fwcdn.pl/fpo/10/48/1048/8064616_1.10.webp"),
        Movie(4, "Parasite", "Thriller", "2019", 9, false, "Polecany przez znajomych.", "https://fwcdn.pl/fpo/81/43/798143/7922316_2.10.webp"),
        Movie(5, "Interstellar", "Sci-Fi", "2014", 9, true, "Sciezka dzwiekowa robi robote.", "https://fwcdn.pl/fpo/56/29/375629/7749216_2.10.webp"),
        Movie(6, "Joker", "Dramat", "2019", 7, false, "", "https://fwcdn.pl/fpo/01/67/810167/7905225_1.10.webp")
    )
}
