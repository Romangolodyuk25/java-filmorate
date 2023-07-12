package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.IdNotExistException;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmDbStorage;
    @Qualifier("UserDbStorage")
    private final UserStorage userDbStorage;
    private final GenreDao genreDao;
    private Integer id = 1;

    @Autowired
    public FilmService(FilmStorage filmDbStorage, UserStorage userDbStorage, GenreDao genreDao) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
        this.genreDao = genreDao;
    }

    public Film createFilm(Film film) {
        checkValidation(film);
        film.setId(id);
        id++;
        filmDbStorage.createFilm(film);
        log.info("Mpa " + film.getMpa());
        return film;
    }

    public Film updateFilm(Film film) {
        checkValidation(film);
        if (filmDbStorage.getFilmById(film.getId()) == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        filmDbStorage.updateFilm(film);
        return film;
    }

    public Film getFilmById(int id) {
        Film receivedFilm = filmDbStorage.getFilmById(id);
        if (receivedFilm == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        return receivedFilm;
    }

    public void deleteFilmById(int id) {
        Film receivedFilm = filmDbStorage.getFilmById(id);
        if (receivedFilm == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        filmDbStorage.deleteFilmById(id);
    }

    public Collection<Film> getAllFilms() {
        return filmDbStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        Film receivedFilm = filmDbStorage.getFilmById(filmId);
        userDbStorage.getUserById(userId);
        receivedFilm.addLike(userId);
        filmDbStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film receivedFilm = filmDbStorage.getFilmById(filmId);
        userDbStorage.getUserById(userId);
        if (!receivedFilm.getLikes().contains(userId)) {
            log.debug("Пользователь с id " + userId + " не ставил оценку фильму");
            throw new IdNotExistException("Данный пользователь не ставил оценку фильму");
        }
        receivedFilm.deleteId(userId);
        filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> getTopFilms(int count) {
        return filmDbStorage.getTopFilms(count);
    }

    public void deleteAllFilms() {
        filmDbStorage.deleteAllFilm();
        id = 1;
    }

    public void checkValidation(Film film) {
        if (film.getName() == null || film.getDescription() == null || film.getReleaseDate() == null ||
                film.getName().isEmpty() || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            log.debug("Фильм содержит некорректные данные: " + film);
            throw new ValidationException("Данные введены неверно");
        }
        List<Integer> ids = film.getGenres().stream()
                .map(f -> film.getId())
                .collect(Collectors.toList());
        List<Genre> genres = ids.stream()
                .map(genreDao::findGenreById)
                .collect(Collectors.toList());
        if (genres.size() != ids.size()) {
            throw new ValidationException("Жанры введены неверно, жанра с таким id не существует");
        }
        film.getGenres().forEach(genre -> {
            try {
                genreDao.findGenreById(genre.getId());
            } catch (Exception e) {
                throw new ValidationException("Жанры введены неверно, жанра с таким id не существует");
            }
        });
    }

}
