package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, Month.JANUARY, 28);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Количество фильмов в хранилище: " + films.size());
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        checkValidation(film);
        film.setId(id);
        films.put(id, film);
        id++;
        log.debug("Фильм " + film + " добавлен в хрнаилище");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        checkValidation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Некорректно введены данные");
            throw new ValidationException("Некорректно введены данные");
        }
        log.debug("Фильм " + film + " обновлен");
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        Film receivedFilm = films.get(id);
        if (id < 1) {
            log.debug("Некорректно введены данные");
            throw new ValidationException("Некорректно введены данные");
        }
        log.debug("Фильм " + receivedFilm + " получен");
        return receivedFilm;
    }

    @Override
    public void deleteFilmById(int id) {
        Film receivedFilm = films.get(id);
        if (id < 1) {
            log.debug("Некорректно введены данные");
            throw new ValidationException("Некорректно введены данные");
        }
        log.debug("Фильм " + receivedFilm + " удален");
        films.remove(id);
    }

    @Override
    public void deleteAllFilm() {
        films.clear();
        id = 1;
    }

    public void checkValidation(Film film) {
        if (film.getName() == null || film.getDescription() == null || film.getReleaseDate() == null ||
                film.getName().isEmpty() || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            log.debug("Фильм содержит некорректные данные: " + film);
            throw new ValidationException("Данные введены неверно");
        }
    }
}
