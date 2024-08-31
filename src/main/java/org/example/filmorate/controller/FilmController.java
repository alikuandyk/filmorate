package org.example.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/film")
    public Film addFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping("/user/{id}")
    public Film updateFilm(@PathVariable int id, @Valid @RequestBody Film updatedFilm) {
        validateFilm(updatedFilm);
        for (Map.Entry<Integer, Film> entry : films.entrySet()) {
            if (entry.getValue().getId() == id) {
                entry.getValue().setTitle(updatedFilm.getTitle());
                entry.getValue().setDescription(updatedFilm.getDescription());
                entry.getValue().setReleaseDate(updatedFilm.getReleaseDate());
                entry.getValue().setDuration(updatedFilm.getDuration());

                log.info("Фильм с id {} обновлен: {}", id, updatedFilm);
                return entry.getValue();
            }
        }
        String errorMsg = "Фильм с ID " + id + " не найден.";
        log.warn(errorMsg);
        throw new ValidationException("Фильм с указанным ID не найден.");
    }

    @GetMapping("/users")
    public List<Film> getAllFilms() {
        List<Film> filmList = new ArrayList<>();

        for (Map.Entry<Integer, Film> entry : films.entrySet()) {
            filmList.add(entry.getValue());
        }

        return filmList;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28).atStartOfDay())) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года.");
        }
    }
}
