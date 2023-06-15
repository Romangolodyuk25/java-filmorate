package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    public Collection<Film> getAllFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public Film getFilmById(int id);

    public void deleteFilmById(int id);

    public void deleteAllFilm();
}
