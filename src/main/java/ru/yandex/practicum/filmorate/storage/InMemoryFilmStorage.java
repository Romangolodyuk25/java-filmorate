package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExist;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Количество фильмов в хранилище: " + films.size());
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.debug("Фильм с таким айди уже существует");
            throw new ObjectAlreadyExist("Данный айди уже существует");
        }
        films.put(film.getId(), film);
        log.debug("Фильм " + film + " добавлен в хрнаилище");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Некорректно введены данные");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        log.debug("Фильм " + film + " обновлен");
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        Film receivedFilm = films.get(id);
        if (receivedFilm == null) {
            log.debug("Некорректно введены данные");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        log.debug("Фильм " + receivedFilm + " получен");
        return receivedFilm;
    }

    @Override
    public void deleteFilmById(int id) {
        Film receivedFilm = films.get(id);
        if (receivedFilm == null) {
            log.debug("Некорректно введены данные");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        log.debug("Фильм " + receivedFilm + " удален");
        films.remove(id);
    }

    @Override
    public void deleteAllFilm() {
        films.clear();
    }
}
