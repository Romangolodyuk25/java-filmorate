package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    public Genre findGenreById(int id) {
        Genre receivedGenre = genreDbStorage.findGenreById(id);
        if (receivedGenre == null) {
            log.info("Жанр с айди " + id + " не существует");
            throw new ObjectNotExistException("Жанр не существует");
        }
        return receivedGenre;
    }
}
