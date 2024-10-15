package org.example.filmorate.storage;

import org.example.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> findAll();
    Optional<Genre> findById(int id);
    List<Genre> getGenresByFilmId(int filmId);
}
