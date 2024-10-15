package org.example.filmorate.storage.impl;

import org.example.filmorate.model.Genre;
import org.example.filmorate.storage.GenreStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("select * from genres order by id", this::rowMapper);
    }

    @Override
    public Optional<Genre> findById(int id) {
        return jdbcTemplate.query("select * from genres where id = ?", this::rowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String sql = """
            SELECT g.id, g.name
            FROM genres g
            JOIN film_genres fg ON g.id = fg.genre_id
            WHERE fg.film_id = ?;
        """;

        return jdbcTemplate.query(sql, this::rowMapper, filmId);
    }

    public Genre rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
