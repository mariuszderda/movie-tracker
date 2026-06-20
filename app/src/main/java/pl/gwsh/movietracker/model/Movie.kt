package pl.gwsh.movietracker.model

data class Movie(
  val id: Int,
  val title: String,
  val genre: String,
  val year: String,
  val rating: Int,
  val watched: Boolean,
  val note: String,
  val posterUrl: String = ""
)

enum class FilterStatus(val label: String) {
  ALL("Wszystkie"),
  TO_WATCH("Do obejrzenia"),
  WATCHED("Obejrzane")
}
