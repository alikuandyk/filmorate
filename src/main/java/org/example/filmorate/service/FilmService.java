package org.example.filmorate.service;

import org.example.filmorate.model.Film;
import org.example.filmorate.storage.FilmStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getAllFilms() {
        return (List<Film>) filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getMostPopulatFilms(int count) {
//        Первое что пришло на ум это получить список всех фильмов от filmStorage,
//        затем сортировать список по количество лайков фильма,
//        и просто вернуть список элементов первых десять фильма

//        то что смог написать
//        List<Film> films = (List<Film>) filmStorage.getAllFilms();
//        films.sort(new Comparator<Film>);

//        мой враг - чатгпт ( я был удивлен и унижен, мне нужно больше практиковаться :/ )
        List<Film> films = (List<Film>) filmStorage.getAllFilms();
        films.sort((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()));

        return films.subList(0, Math.min(count, films.size()));
    }
}
