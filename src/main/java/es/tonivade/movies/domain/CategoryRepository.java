package es.tonivade.movies.domain;

import java.util.Set;

public interface CategoryRepository {

  Set<String> findCategories();

}
