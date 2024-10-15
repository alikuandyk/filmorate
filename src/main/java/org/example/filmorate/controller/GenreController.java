package org.example.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.filmorate.exception.EntityNotFoundException;
import org.example.filmorate.model.Genre;
import org.example.filmorate.storage.impl.GenreDbStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class GenreController {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @GetMapping("/genres")
    public List<Genre> findAll() {
        return genreDbStorage.findAll();
    }

    @GetMapping("/genres/{id}")
    public Genre findById(@PathVariable int id) {
        return genreDbStorage.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Жанр с ID " + id + " не найден"));
    }
}
