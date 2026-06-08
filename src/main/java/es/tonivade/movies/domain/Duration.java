package es.tonivade.movies.domain;

public record Duration(int value) {

  public Duration {
    if (value < 1) {
      throw new IllegalArgumentException("invalid duration: " + value);
    }
  }

}
