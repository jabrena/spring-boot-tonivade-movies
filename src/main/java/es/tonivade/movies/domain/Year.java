package es.tonivade.movies.domain;

public record Year(int value) {

  public Year {
    if (value < 1900 || value > 2026) {
      throw new IllegalArgumentException("invalid year: " + value);
    }
  }
}
