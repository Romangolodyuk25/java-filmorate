package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
        if (receivedFilm == null || filmId < 1 || userId < 1) {
            log.debug("Некорректно введены данные");
            throw new ObjectNotExistException("Переданный айди фильма не найден");
        }
        receivedFilm.addLike(userId);
        inMemoryFilmStorage.updateFilm(receivedFilm);
    }

    public void deleteLike(int filmId, int userId) {
        Film receivedFilm = inMemoryFilmStorage.getFilmById(filmId);
        if (receivedFilm == null || filmId < 1 || userId < 1) {
            log.debug("Некорректно введены данные");
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
        List<Film> films = new ArrayList<>(inMemoryFilmStorage.getAllFilms());
        return films.stream()
                .sorted((f1, f2) -> f2.getAllLikes().size() - f1.getAllLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        //        List<Film> sortedFilm = new ArrayList<>(inMemoryFilmStorage.getAllFilms());
//        sortedFilm.sort(new Comparator<Film>() {
//            @Override
//            public int compare(Film film1, Film film2) {
//                return film2.getAllLikes().size() - film1.getAllLikes().size();
//            }
//        });
//        if (count == 10) {
//            return new ArrayList<>(inMemoryFilmStorage.getAllFilms());
//        }
//        return sortedFilm.subList(0, count);
    }

    public void deleteAllFilms() {
        inMemoryFilmStorage.deleteAllFilm();
    }

}
