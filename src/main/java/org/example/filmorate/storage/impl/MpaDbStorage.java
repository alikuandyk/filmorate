package org.example.filmorate.storage.impl;

import org.example.filmorate.model.Mpa;
import org.example.filmorate.storage.MpaStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("select * from mpa", this::rowMapper);
    }

    @Override
    public Optional<Mpa> findById(int id) {
        return jdbcTemplate.query("select * from mpa where mpa.id = ?", this::rowMapper, id)
                .stream()
                .findFirst();
    }

    public Mpa rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(
                rs.getInt("id"),
                rs.getString("rating")
        );
    }
}
