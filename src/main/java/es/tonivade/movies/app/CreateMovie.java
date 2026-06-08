package es.tonivade.movies.app;

import static es.tonivade.movies.domain.MovieValidator.validate;

import java.util.Optional;

import org.springframework.stereotype.Component;

import es.tonivade.movies.MovieResource;
import es.tonivade.movies.domain.Cast;
import es.tonivade.movies.domain.CategoryRepository;
import es.tonivade.movies.domain.CreateMovieResult;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.DuplicatedMovie;
import es.tonivade.movies.domain.Duration;
import es.tonivade.movies.domain.Movie;
import es.tonivade.movies.domain.MovieRepository;
import es.tonivade.movies.domain.Stars;
import es.tonivade.movies.domain.Year;

@Component
public class CreateMovie {

  private final CategoryRepository categoryRepository;
  private final MovieRepository movieRepository;

  public CreateMovie(CategoryRepository categoryRepository, MovieRepository movieRepository) {
    this.categoryRepository = categoryRepository;
    this.movieRepository = movieRepository;
  }

  public CreateMovieResult execute(MovieResource movie) {
    return validate(movie, categoryRepository.findCategories())
        .or(() -> checkDuplicatedMovie(movie.title()))
        .orElseGet(() -> create(movie));
  }

  private CreateMovieResult create(MovieResource movie) {
    return movieRepository.create(convert(movie));
  }

  private Movie convert(MovieResource movie) {
    return new Movie(
        null,
        movie.title(),
        new Year(movie.year()),
        new Duration(movie.duration()),
        movie.director(),
        new Cast(movie.cast()),
        Stars.values()[movie.stars() - 1],
        movie.category());
  }

  private Optional<DuplicatedMovie> checkDuplicatedMovie(String title) {
    return movieRepository.findByTitle(title).map(DuplicatedMovie::new);
  }
}
