package org.example.filmorate.storage;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.filmorate.model.Film;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int uniqueId = 1;

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);

        film.setId(uniqueId++);
        films.put(film.getId(), film);

        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.warn("Фильм с ID " + film.getId() + " не найден");
            throw new ValidationException("Фильм с указанным ID не найден");
        }
    }

    @Override
    public void deleteFilm(int id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            log.warn("Фильм с ID " + id + " не найден");
            throw new ValidationException("Фильм с указанным ID не найден");
        }
    }

    @Override
    public Film getFilmById(int id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            log.warn("Фильм с ID " + id + " не найден");
            throw new ValidationException("Фильм с указанным ID не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года.");
        }
    }
}
