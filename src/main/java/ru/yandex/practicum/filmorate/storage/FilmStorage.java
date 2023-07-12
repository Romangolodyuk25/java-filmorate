package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    void deleteFilmById(int id);

    void deleteAllFilm();

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getTopFilms(int count);
}
