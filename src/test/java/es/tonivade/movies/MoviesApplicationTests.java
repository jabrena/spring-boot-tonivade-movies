/*
 * Copyright (c) 2026, Antonio Gabriel Muñoz Conejo <me at tonivade dot es>
 * Distributed under the terms of the MIT License
 */
package es.tonivade.movies;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import es.tonivade.movies.infra.InMemoryMovieRepository;

@SpringBootTest
@AutoConfigureMockMvc
class MoviesApplicationTests {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  InMemoryMovieRepository repository;

  @BeforeEach
  void setUp() {
    repository.reset();
  }

  @Test
  void shouldGetMovies() throws Exception {
    mockMvc.perform(get("/movies"))
      .andExpect(status().isOk())
      .andExpect(content().json("""
        [
          {
            "id": 1,
            "title": "Lord of the Rings",
            "director": "Peter Jackson",
            "year": 2001,
            "duration": 178,
            "stars": 5,
            "cast": [
              "Elijah Wood",
              "Ian McKellen",
              "Orlando Bloom",
              "Viggo Mortensen",
              "Sam Bean"
            ],
            "category": "Fantasy"
          }
        ]"""));
  }

  @Test
  void shouldGetMovieById() throws Exception {
    mockMvc.perform(get("/movies/{id}", 1))
      .andExpect(status().isOk())
      .andExpect(content().json("""
        {
          "id": 1,
          "title": "Lord of the Rings",
          "director": "Peter Jackson",
          "year": 2001,
          "duration": 178,
          "stars": 5,
          "cast": [
            "Elijah Wood",
            "Ian McKellen",
            "Orlando Bloom",
            "Viggo Mortensen",
            "Sam Bean"
          ],
          "category": "Fantasy"
        }"""));
  }

  @Test
  void shouldCreateMovies() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "director": "Wachowski brothers",
          "duration": 136,
          "stars": 5,
          "title": "Matrix",
          "year": 1999,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isOk())
      .andExpect(content().json("2"));
  }

  @Test
  void shouldDeleteMovies() throws Exception {
    mockMvc.perform(delete("/movies/{id}", 1))
      .andExpect(status().isOk());
  }

  @Test
  void shouldReturnErrorWhenEmptyTitle() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "director": "Wachowski brothers",
          "duration": 136,
          "stars": 5,
          "year": 1999,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "title cannot be empty"
        }"""));
  }

  @Test
  void shouldReturnErrorWhenEmptyDirector() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "duration": 136,
          "stars": 5,
          "title": "Matrix",
          "year": 1999,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "director cannot be empty"
        }"""));
  }

  @Test
  void shouldReturnErrorWhenInvalidCategory() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "director": "Wachowski brothers",
          "duration": 136,
          "stars": 5,
          "title": "Matrix",
          "year": 1999,
          "category": "ksjdfd"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "invalid category: ksjdfd"
        }"""));
  }

  @Test
  void shouldReturnErrorWhenInvalidYear() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "director": "Wachowski brothers",
          "duration": 136,
          "stars": 5,
          "title": "Matrix",
          "year": 1801,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "invalid year: 1801"
        }"""));
  }

  @Test
  void shouldReturnErrorWhenInvalidDuration() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "director": "Wachowski brothers",
          "duration": 0,
          "stars": 5,
          "title": "Matrix",
          "year": 1999,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "invalid duration: 0"
        }"""));
  }

  @Test
  void shouldReturnErrorWhenInvalidStars() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "director": "Wachowski brothers",
          "duration": 136,
          "stars": 0,
          "title": "Matrix",
          "year": 1999,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "invalid stars: 0"
        }"""));
  }

  @Test
  void shouldReturnErrorWhenEmptyCast() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
          ],
          "director": "Wachowski brothers",
          "duration": 136,
          "stars": 5,
          "title": "Matrix",
          "year": 1999,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "cast cannot be empty"
        }"""));
  }

  @Test
  void shouldReturnErrorWhenDuplicatedActor() throws Exception {
    mockMvc.perform(post("/movies")
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content("""
        {
          "cast": [
            "Keanu Reeves",
            "Keanu Reeves",
            "Carrie-Anne Moss",
            "Laurence Fishbone"
          ],
          "director": "Wachowski brothers",
          "duration": 136,
          "stars": 5,
          "title": "Matrix",
          "year": 1999,
          "category": "Sci-Fi"
        }"""))
      .andExpect(status().isBadRequest())
      .andExpect(content().json("""
        {
          "detail": "duplicated actor: Keanu Reeves"
        }"""));
  }
}
