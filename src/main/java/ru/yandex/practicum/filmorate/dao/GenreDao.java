package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    Genre findGenreById(int id);

    List<Genre> getAllGenres();

    List<Genre> findByIds(List<Integer> ids);

    void setFilmAndGenre(Film film);

    void saveGenre(Genre genre);
}
