package es.tonivade.movies.domain;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import es.tonivade.movies.MovieResource;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.DuplicatedActor;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.EmptyCast;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.EmptyDirector;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.EmptyTitle;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidCategory;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidDuration;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidStars;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidYear;

public class MovieValidator {

  public static Optional<CreateMovieResult> validate(MovieResource movie, Set<String> categories) {
    if (movie.year() == null || movie.year() < 1900 || movie.year() > 2026) {
      return Optional.of(new InvalidYear(movie.year()));
    }
    if (movie.duration() == null || movie.duration() < 1) {
      return Optional.of(new InvalidDuration(movie.duration()));
    }
    if (movie.stars() == null || movie.stars() < 1 || movie.stars() > 5) {
      return Optional.of(new InvalidStars(movie.stars()));
    }
    if (movie.title() == null || movie.title().isBlank()) {
      return Optional.of(new EmptyTitle());
    }
    if (movie.director() == null || movie.director().isBlank()) {
      return Optional.of(new EmptyDirector());
    }
    if (movie.cast() == null || movie.cast().isEmpty()) {
      return Optional.of(new EmptyCast());
    }
    if (movie.category() == null || !categories.contains(movie.category())) {
      return Optional.of(new InvalidCategory(movie.category()));
    }
    return findDuplicates(movie.cast()).map(DuplicatedActor::new);
  }

  private static Optional<String> findDuplicates(Collection<String> cast) {
    var groupingBy = cast.stream()
      .collect(groupingBy(identity(), counting()));
    return groupingBy.entrySet().stream()
      .filter(entry -> entry.getValue() > 1)
      .map(Map.Entry::getKey).findFirst();
  }
}
