package org.example.filmorate.storage.impl;

import jakarta.validation.ValidationException;
import org.example.filmorate.exception.EntityNotFoundException;
import org.example.filmorate.model.Film;
import org.example.filmorate.model.Genre;
import org.example.filmorate.model.Mpa;
import org.example.filmorate.storage.FilmStorage;
import org.example.filmorate.storage.GenreStorage;
import org.example.filmorate.storage.MpaStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    private static final String SELECT = """
            select films.id,
                   films.title,
                   films.description,
                   films.release_date,
                   films.duration,
                   mpa.id as mpa_id,
                   mpa.rating
            
            from films
            join mpa
            on films.mpa_id = mpa.id""";

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    @Transactional
    public Film addFilm(Film film) {
        mpaStorage.findById(film.getMpa().getId()).orElseThrow(() -> new ValidationException("MPA с таким ID не найден"));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> map = Map.of(
                "title", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration(),
                "mpa_id", film.getMpa().getId()
        );

        int id = insert.executeAndReturnKey(map).intValue();
        film.setId(id);

        String sql = "insert into film_genres (film_id, genre_id) values (?, ?)";
        film.getGenres().forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre.getId()));

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int result = jdbcTemplate.update("update films set title = ?, description = ?, release_date = ?, duration = ? where films.id = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());

        if (result == 0) {
            throw new EntityNotFoundException("фильм не найден");
        }

        return film;
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update("delete from films where films.id = ?", id);
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(SELECT, this::rowMapper);
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = getFilmById(filmId);

        String sql = "insert into likes (user_id, film_id) values (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);

        film.getLikes().add(userId);
        this.updateFilm(film);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sql = SELECT + """
                 left join likes on films.ID = likes.FILM_ID
                group by films.id
                order by count(LIKES.FILM_ID) desc limit ?
                """;

        return jdbcTemplate.query(sql, this::rowMapper, count);
    }

    @Override
    public Film getFilmById(int id) {
        return jdbcTemplate.queryForObject(SELECT + " where films.id = ?", this::rowMapper, id);
    }

    @Override
    public Film getFilmWithGenresById(int id) {
        Film film = getFilmById(id);
        if (film != null) {
            List<Genre> genres = genreStorage.getGenresByFilmId(id);
            film.getGenres().addAll(genres);
            return film;
        }
        return null;
    }

    public Film rowMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(
                        rs.getInt("mpa_id"),
                        rs.getString("rating")
                )
        );
    }
}
