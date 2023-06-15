package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film receivedFilm = inMemoryFilmStorage.getFilmById(filmId);
        if (receivedFilm == null) {
            log.debug("Фильм с id " + filmId + " не существует");
            throw new ObjectNotExistException("Переданный айди фильма не найден");
        }
        receivedFilm.addLike(userId);
        inMemoryFilmStorage.updateFilm(receivedFilm);
    }

    public void deleteLike(int filmId, int userId) {
        Film receivedFilm = inMemoryFilmStorage.getFilmById(filmId);
        if (receivedFilm == null) {
            log.debug("Фильм с id " + filmId + " не существует");
            throw new ObjectNotExistException("Переданный айди фильма не найден");
        }
        if (!receivedFilm.getAllLikes().contains(userId)) {
            log.debug("Пользователь с id " + userId + " не ставил оценку фильму");
            throw new ObjectNotExistException("Некорректный айди пользователя");
        }
        receivedFilm.deleteId(userId);
        inMemoryFilmStorage.updateFilm(receivedFilm);
    }

    public List<Film> getTopFilms(int count) {
        List<Film> sortedFilm = new ArrayList<>(inMemoryFilmStorage.getAllFilms());
        sortedFilm.sort(new Comparator<Film>() {
            @Override
            public int compare(Film film1, Film film2) {
                return film2.getAllLikes().size() - film1.getAllLikes().size();
            }
        });
        return sortedFilm.subList(0, count);
    }

    public void deleteAllFilms() {
        inMemoryFilmStorage.deleteAllFilm();
    }

}
