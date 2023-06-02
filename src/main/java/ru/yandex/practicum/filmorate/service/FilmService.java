package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Service
public class FilmService {
    private final HashMap<Integer, Film> films = new HashMap<>();
    public static Integer id = 1;


    @GetMapping
    public Collection<Film> getAllFilms(){
        log.debug("Количество фильмов в хранилище: " + films.size());
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film){
        LocalDate checkDateRelease = LocalDate.of(1895, Month.JANUARY, 28);

        if (film.getName().isEmpty() || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(checkDateRelease) || film.getDuration() <= 0){
            log.debug("Объект содержит некорректные данные: " + film.toString());
            throw new ValidationException("Данные введене неверно");
        }
        film.setId(id);
        films.put(id, film);
        id++;
        log.debug("Фильм " + film.toString() + " добавлен в хрнаилище");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){//узнать насчет правильности написания
        LocalDate checkDateRelease = LocalDate.of(1895, Month.JANUARY, 28);

        if (film.getName().isEmpty() || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(checkDateRelease) || film.getDuration() <= 0){
            log.debug("Объект содержит некорректные данные: " + film.toString());
            throw new ValidationException("Данные введены неверно");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Фильм с данным айди не существует");
            throw new ValidationException("Некорректно введены данные");
        }
        log.debug("Фильм " + film.toString() + " обновлен");
        return film;
    }
}
