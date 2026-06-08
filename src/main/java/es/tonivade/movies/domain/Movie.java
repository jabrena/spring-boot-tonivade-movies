package es.tonivade.movies.domain;

import org.jspecify.annotations.Nullable;

public record Movie(
  @Nullable Integer id,
  String title,
  Year year,
  Duration duration,
  String director,
  Cast cast,
  Stars stars,
  String category) {

  public Movie {
    if (title.isBlank()) {
      throw new IllegalArgumentException("title cannot be blank");
    }
    if (director.isBlank()) {
      throw new IllegalArgumentException("director cannot be blank");
    }
    if (category.isBlank()) {
      throw new IllegalArgumentException("category cannot be blank");
    }
  }

  public Movie withId(int id) {
    return new Movie(id, title, year, duration, director, cast, stars, category);
  }
}
