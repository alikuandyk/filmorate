package org.example.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    List<Film> films = new ArrayList<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        films.add(film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @PutMapping("/{id}")
    public Film updateFilm(@PathVariable int id, @Valid @RequestBody Film updatedFilm) {
        for (Film film : films) {
            if (film.getId() == id) {
                film.setTitle(updatedFilm.getTitle());
                film.setDescription(updatedFilm.getDescription());
                film.setReleaseDate(updatedFilm.getReleaseDate());
                film.setDuration(updatedFilm.getDuration());
                log.info("Фильм с id {} обновлен: {}", id, updatedFilm);
                return film;
            }
        }
        String errorMsg = "Фильм с ID " + id + " не найден.";
        log.error(errorMsg);
        return null;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }
}
