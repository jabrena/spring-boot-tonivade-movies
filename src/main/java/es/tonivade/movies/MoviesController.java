package es.tonivade.movies;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Collection;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.tonivade.movies.app.CreateMovie;
import es.tonivade.movies.domain.CreateMovieResult.MovieCreated;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.DuplicatedActor;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.DuplicatedMovie;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.EmptyCast;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.EmptyDirector;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.EmptyTitle;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidCategory;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidDuration;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidStars;
import es.tonivade.movies.domain.CreateMovieResult.ValidationError.InvalidYear;
import es.tonivade.movies.domain.Movie;
import es.tonivade.movies.domain.MovieRepository;

@RestController
public class MoviesController {

  private final CreateMovie create;
  private final MovieRepository repository;

  public MoviesController(CreateMovie create, MovieRepository repository) {
    this.create = create;
    this.repository = repository;
  }

  @GetMapping("/movies")
  public ResponseEntity<Collection<MovieResource>> list() {
    return ResponseEntity.ok(repository.findAll().stream().map(MoviesController::convert).toList());
  }

  @GetMapping("/movies/{id}")
  public ResponseEntity<MovieResource> get(@PathVariable("id") int id) {
    return ResponseEntity.of(repository.findById(id).map(MoviesController::convert));
  }

  @PostMapping("/movies")
  public ResponseEntity<Integer> create(@RequestBody MovieResource movie) {
    var result = create.execute(movie);
    return switch (result) {
      case MovieCreated(var id) -> ResponseEntity.ok(id);
      case ValidationError e -> ResponseEntity.of(toProblem(e)).build();
    };
  }

  static ProblemDetail toProblem(ValidationError error) {
    return switch (error) {
      case DuplicatedMovie(int id) -> {
        var problem = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "duplicated movie with id: " + id);
        problem.setProperty("movie", "/movies/" + id);
        yield problem;
      }
      case InvalidDuration(var duration) -> {
        var problem = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "invalid duration: " + duration);
        problem.setProperty("duration", duration);
        yield problem;
      }
      case InvalidYear(var year) -> {
        var problem = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "invalid year: " + year);
        problem.setProperty("year", year);
        yield problem;
      }
      case InvalidStars(var stars) -> {
        var problem = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "invalid stars: " + stars);
        problem.setProperty("stars", stars);
        yield problem;
      }
      case DuplicatedActor(var actor) -> {
        var problem = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "duplicated actor: " + actor);
        problem.setProperty("actor", actor);
        yield problem;
      }
      case InvalidCategory(var category) -> {
        var problem = ProblemDetail.forStatusAndDetail(BAD_REQUEST, "invalid category: " + category);
        problem.setProperty("category", category);
        yield problem;
      }
      case EmptyTitle() -> ProblemDetail.forStatusAndDetail(BAD_REQUEST, "title cannot be empty");
      case EmptyDirector() -> ProblemDetail.forStatusAndDetail(BAD_REQUEST, "director cannot be empty");
      case EmptyCast() -> ProblemDetail.forStatusAndDetail(BAD_REQUEST, "cast cannot be empty");
    };
  }

  @DeleteMapping("/movies/{id}")
  public void delete(@PathVariable("id") int id) {
    repository.delete(id);
  }

  private static MovieResource convert(Movie movie) {
    return new MovieResource(
        movie.id(),
        movie.title(),
        movie.year().value(),
        movie.duration().value(),
        movie.director(),
        movie.cast().value(),
        movie.stars().ordinal() + 1,
        movie.category());
  }
}
