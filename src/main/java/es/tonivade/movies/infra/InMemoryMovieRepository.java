package es.tonivade.movies.infra;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

import es.tonivade.movies.domain.Cast;
import es.tonivade.movies.domain.CreateMovieResult;
import es.tonivade.movies.domain.CreateMovieResult.MovieCreated;
import es.tonivade.movies.domain.Duration;
import es.tonivade.movies.domain.Movie;
import es.tonivade.movies.domain.MovieRepository;
import es.tonivade.movies.domain.Stars;
import es.tonivade.movies.domain.Year;

@Repository
public class InMemoryMovieRepository implements MovieRepository {

  private static final Movie LOTR = new Movie(
    null,
    "Lord of the Rings",
    new Year(2001),
    new Duration(178),
    "Peter Jackson",
    new Cast(List.of("Elijah Wood", "Ian McKellen", "Orlando Bloom", "Viggo Mortensen", "Sam Bean")),
    Stars.FIVE,
    "Fantasy");
  private final AtomicInteger counter = new AtomicInteger();
  private final Map<Integer, Movie> db = new HashMap<>();

  @Override
  public Collection<Movie> findAll() {
    return db.values();
  }

  @Override
  public Optional<Movie> findById(int id) {
    return Optional.ofNullable(db.get(id));
  }

  @Override
  public CreateMovieResult create(Movie movie) {
    var id = counter.incrementAndGet();
    db.put(id, movie.withId(id));
    return new MovieCreated(id);
  }

  @Override
  public void delete(int id) {
    db.remove(id);
  }

  public void reset() {
    db.clear();
    counter.set(0);
    create(LOTR);
  }

  @Override
  public Optional<Integer> findByTitle(String title) {
    return db.values().stream()
      .filter(m -> m.title().equals(title))
      .map(Movie::id)
      .findFirst();
  }
}