package org.example.filmorate.storage;

import org.example.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    void deleteFilm(int id);
    Film getFilmById(int id);
    Collection<Film> getAllFilms();
}
