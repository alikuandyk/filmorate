package org.example.filmorate.storage;

import org.example.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    void deleteFilm(int id);
    Film getFilmById(int id);
    List<Film> getAllFilms();
    List<Film> getMostPopularFilms(int count);
    void addLike(int filmId, int userId);
}
