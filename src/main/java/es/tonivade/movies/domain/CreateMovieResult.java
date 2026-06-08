package es.tonivade.movies.domain;

public sealed interface CreateMovieResult {

  record MovieCreated(int id) implements CreateMovieResult {}

  sealed interface ValidationError extends CreateMovieResult {
    record DuplicatedMovie(int id) implements ValidationError {}
    record InvalidDuration(int duration) implements ValidationError {}
    record InvalidYear(int year) implements ValidationError {}
    record InvalidStars(int stats) implements ValidationError {}
    record EmptyTitle() implements ValidationError {}
    record EmptyDirector() implements ValidationError {}
    record EmptyCast() implements ValidationError {}
    record DuplicatedActor(String actor) implements ValidationError {}
    record InvalidCategory(String category) implements ValidationError {}
  }
}
