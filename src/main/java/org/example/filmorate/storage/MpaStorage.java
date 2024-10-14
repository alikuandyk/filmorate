package org.example.filmorate.storage;

import org.example.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    List<Mpa> findAll();
    Optional<Mpa> findById(int id);
}
