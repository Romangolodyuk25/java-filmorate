package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class GenreDbStorage implements GenreDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Genre findGenreById(int id) {
        String sql = "SELECT genre_id, name " +
                "FROM GENRES " +
                "WHERE genre_id = ? ";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        if (genres.size() != 1) {
            return null;
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT genre_id, name " +
                "FROM GENRES ";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
        if (genres.size() == 0) {
            log.info("В таблице GENRES нет жанров");
            return null;
        }
        return genres;
    }

    @Override
    public List<Genre> findByIds(List<Integer> ids) {
        String sql = "SELECT genre_id, name " +
                "FROM GENRES " +
                "WHERE genre_id in (?) ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), ids);
    }

    @Override
    public void saveGenre(Genre genre) {
        String sql = "INSERT INTO GENRES (name) " +
                "VALUES (?) ";
        jdbcTemplate.update(sql, genre.getName());
    }

    @Override
    public void setFilmAndGenre(Film film) {
        String deleteSql = "DELETE FROM GENRE_AND_FILM " +
                "WHERE film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());
        String sql = "INSERT INTO GENRE_AND_FILM (film_id, genre_id) " +
                "VALUES (?, ?)";
        List<Integer> genres = film.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
        genres.forEach(genre -> jdbcTemplate.update(sql, film.getId(), genre));
    }

    public Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }
}
