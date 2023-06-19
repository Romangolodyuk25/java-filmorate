package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FilmService {

    private static final LocalDate MIN_DATE_RELEASE = LocalDate.of(1895, Month.JANUARY, 28);
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;
    private Integer id = 1;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film createFilm(Film film) {
        checkValidation(film);
        film.setId(id);
        id++;
        inMemoryFilmStorage.createFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        checkValidation(film);
        if (inMemoryFilmStorage.getFilmById(film.getId()) == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    public Film getFilmById(int id) {
        Film receivedFilm = inMemoryFilmStorage.getFilmById(id);
        if (receivedFilm == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        return receivedFilm;
    }

    public void deleteFilmById(int id) {
        Film receivedFilm = inMemoryFilmStorage.getFilmById(id);
        if (receivedFilm == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        inMemoryFilmStorage.deleteFilmById(id);
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        Film receivedFilm = inMemoryFilmStorage.getFilmById(filmId);
        inMemoryUserStorage.getUserById(userId);// выбросит исключение в случает отсутствия юзера
        receivedFilm.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film receivedFilm = inMemoryFilmStorage.getFilmById(filmId);
        inMemoryUserStorage.getUserById(userId);
        if (!receivedFilm.getLikes().contains(userId)) {
            log.debug("Пользователь с id " + userId + " не ставил оценку фильму");
            throw new IdNotExistException("Данный пользователь не ставил оценку фильму");
        }
        receivedFilm.deleteId(userId);
    }

    public List<Film> getTopFilms(int count) {
        return inMemoryFilmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteAllFilms() {
        inMemoryFilmStorage.deleteAllFilm();
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
