package org.example.filmorate.storage;

import org.example.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findAll();
    Genre findById(int id);
}
