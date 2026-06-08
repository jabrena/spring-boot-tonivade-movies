/*
 * Copyright (c) 2026, Antonio Gabriel Muñoz Conejo <me at tonivade dot es>
 * Distributed under the terms of the MIT License
 */
package es.tonivade.movies;

import java.util.List;

import org.jspecify.annotations.Nullable;

public record MovieResource(
    @Nullable Integer id,
    @Nullable String title,
    @Nullable Integer year,
    @Nullable Integer duration,
    @Nullable String director,
    @Nullable List<String> cast,
    @Nullable Integer stars,
    @Nullable String category) {

  public MovieResource {
    cast = cast != null ? List.copyOf(cast) : List.of();
  }
}
