package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public class FilmDbStorage implements FilmStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT * " +
                     "FROM FILMS " +
                     "ORDER BY film_id ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO FILMS (name, description, releaseDate, duration, rating) " +
                     "VALUES (?, ?, ?, ?, ?) ";
        int filmId = jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRating());
        return new Film(filmId, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRating());
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILMS SET name = ?, description = ?, releaseDate = ?, duration = ?, rating = ? " +
                     "WHERE film_id = ? ";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getDescription(),
                                 film.getDuration(), film.getRating(), film.getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT film_id, name, description, releaseDate, duration, rating " +
                     "FROM FILMS WHERE film_id = ? ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id).get(id);
    }

    @Override
    public void deleteFilmById(int id) {
        String sql = "DELETE FROM FILMS " +
                     "WHERE film_id = ? ";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAllFilm() {
        String sql = "TRUNCATE TABLE FILMS ";
        jdbcTemplate.update(sql);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        int duration = rs.getInt("id");
        String rating = rs.getString("rating");

        return new Film(filmId, name, description, releaseDate, duration, FilmRating.valueOf(rating));
    }
}
