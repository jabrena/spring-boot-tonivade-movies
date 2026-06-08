package es.tonivade.movies.domain;

import java.util.List;

public record Cast(List<String> value) {

  public Cast {
    if (value.isEmpty()) {
      throw new IllegalArgumentException("invalid cast: " + value);
    }
    if (containsDuplicates(value)) {
      throw new IllegalArgumentException("invalid cast: " + value);
    }
    value = List.copyOf(value);
  }

  private static boolean containsDuplicates(List<String> value) {
    return value.stream().distinct().count() != value.size();
  }
}
