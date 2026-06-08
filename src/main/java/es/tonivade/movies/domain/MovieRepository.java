package es.tonivade.movies.domain;

import java.util.Collection;
import java.util.Optional;

public interface MovieRepository {

  Collection<Movie> findAll();

  Optional<Movie> findById(int id);

  CreateMovieResult create(Movie movie);

  void delete(int id);

  Optional<Integer> findByTitle(String title);

}