/*
 * Copyright (c) 2026, Antonio Gabriel Muñoz Conejo <me at tonivade dot es>
 * Distributed under the terms of the MIT License
 */
package es.tonivade.movies.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import es.tonivade.movies.MovieResource;
import es.tonivade.movies.domain.Cast;
import es.tonivade.movies.domain.CategoryRepository;
import es.tonivade.movies.domain.CreateMovieResult.MovieCreated;
import es.tonivade.movies.domain.Duration;
import es.tonivade.movies.domain.Movie;
import es.tonivade.movies.domain.MovieRepository;
import es.tonivade.movies.domain.Stars;
import es.tonivade.movies.domain.Year;

@MockitoSettings
class CreateMovieTest {

  private final CategoryRepository categoryRepository;
  private final MovieRepository movieRepository;
  private final CreateMovie useCase;

  public CreateMovieTest(@Mock CategoryRepository categoryRepository, @Mock MovieRepository movieRepository) {
    this.categoryRepository = categoryRepository;
    this.movieRepository = movieRepository;
    this.useCase = new CreateMovie(categoryRepository, movieRepository);
  }

  @Test
  void shouldCreateMovie() {
    var resource = new MovieResource(
        null,
        "Lord of the Rings",
        2001,
        178,
        "Peter Jackson",
        List.of("Elijah Wood", "Ian McKellen", "Orlando Bloom", "Viggo Mortensen", "Sam Bean"),
        5,
        "Fantasy");
    var movie = new Movie(
        null,
        "Lord of the Rings",
        new Year(2001),
        new Duration(178),
        "Peter Jackson",
        new Cast(List.of("Elijah Wood", "Ian McKellen", "Orlando Bloom", "Viggo Mortensen", "Sam Bean")),
        Stars.FIVE,
        "Fantasy");
    when(categoryRepository.findCategories())
      .thenReturn(Set.of("Fantasy", "Action", "Comedy"));
    when(movieRepository.findByTitle(resource.title()))
      .thenReturn(Optional.empty());
    when(movieRepository.create(movie))
      .thenReturn(new MovieCreated(1));

    var result = useCase.execute(resource);

    assertThat(result).isEqualTo(new MovieCreated(1));
  }
}
