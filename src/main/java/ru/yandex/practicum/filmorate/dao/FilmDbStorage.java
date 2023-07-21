package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    GenreDbStorage genreDbStorage;
    MpaDbStorage mpaDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration, f.rate, f.mpa_id, m.name AS mpa_name " +
                     "FROM FILMS AS f " +
                     "LEFT JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
                     "ORDER BY film_id ";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        if (films.size() == 0) {
            log.info("Таблица FILMS пустая");
        }
        loadFilmGenre(films);
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO FILMS (name, description, releaseDate, duration, rate, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) ";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId());
        log.info("Фильм " + film + " создан");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILMS SET name = ?, description = ?, releaseDate = ?, duration = ?, rate = ?, mpa_id = ? " +
                "WHERE film_id = ? ";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        log.info("Фильм " + film + " обновлен");
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration, f.rate, f.mpa_id, m.name AS mpa_name " +
                "FROM FILMS AS f " +
                "LEFT JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = ? " +
                "GROUP BY f.film_id ";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
        loadFilmGenre(films);
        if (films.size() != 1) {
            log.info("Фильм с айди " + id + " не найден");
            throw new ObjectNotExistException("Фильм не неайден");
        }
        return films.get(0);
    }

    @Override
    public void deleteFilmById(int id) {
        String sql = "DELETE FROM FILMS " +
                "WHERE film_id = ? ";
        getFilmById(id);
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAllFilm() {
        String sql = "DELETE FROM FILMS ";
        jdbcTemplate.update(sql);
    }

    public void loadFilmGenre(List<Film> films) {
        final List<Integer> filmsId = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());
        String sql = "SELECT * " +
                "FROM GENRE_AND_FILM AS gaf " +
                "JOIN GENRES AS g ON gaf.genre_id = g.genre_id " +
                "WHERE film_id = ? ";
        films.forEach(film -> {
            List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> genreDbStorage.makeGenre(rs), film.getId());
            film.addGenre(genres);
        });
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO FAVOURITE_FILM (film_id, user_id)" +
                     "VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM FAVOURITE_FILM " +
                "WHERE film_id = ? AND user_id = ? ";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sql = "SELECT f.film_id, f.name, f.description, f.releaseDate, f.duration, f.rate, f.mpa_id, m.name AS mpa_name " +
                "FROM FILMS AS f LEFT JOIN (SELECT ff.film_id, count(*) AS count " +
                "FROM FAVOURITE_FILM ff GROUP BY ff.film_id) rating ON f.film_id = rating.film_id LEFT JOIN MPA AS m ON f.mpa_id = m.mpa_id WHERE f.rate IS NOT NULL ORDER BY rating.count DESC";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        if (films.size() > count) {
            films = films.subList(0, count);
        }
        log.info("Количество популярных фильмов " + films.size());
        return films;
    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                new HashSet<>(),
                rs.getInt("rate"),
                new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"))
        );
    }
}
