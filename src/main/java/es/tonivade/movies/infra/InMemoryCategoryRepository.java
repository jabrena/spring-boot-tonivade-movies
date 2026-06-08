package es.tonivade.movies.infra;

import java.util.Set;

import org.springframework.stereotype.Repository;

import es.tonivade.movies.domain.CategoryRepository;

@Repository
public class InMemoryCategoryRepository implements CategoryRepository {

  @Override
  public Set<String> findCategories() {
    return Set.of("Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Fantasy", "Thriller");
  }
}
