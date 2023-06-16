package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class FilmServiceTest {
    private final FilmService filmService;
    private final FilmStorage inMemoryFilmStorage;
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmServiceTest(FilmService filmService, FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.filmService = filmService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @BeforeEach
    public void beforeEach() {
        filmService.deleteAllFilms();
    }

    @Test
    public void shouldAddLike() {
        Film film = inMemoryFilmStorage.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        User user = inMemoryUserStorage.createUser(new User(5, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25), new HashSet<>()));

        assertEquals(0, film.getAllLikes().size());

        filmService.addLike(1, 5);
        assertEquals(1, film.getAllLikes().size());
        assertTrue(film.getAllLikes().contains(5));
    }

    @Test
    public void shouldDeleteLike() {
        Film film = inMemoryFilmStorage.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        User user = inMemoryUserStorage.createUser(new User(5, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25), new HashSet<>()));

        assertEquals(0, film.getAllLikes().size());

        filmService.addLike(1, 5);
        assertEquals(1, film.getAllLikes().size());
        assertTrue(film.getAllLikes().contains(5));

        filmService.deleteLike(1, 5);
        assertEquals(0, film.getAllLikes().size());
    }

    @Test
    public void shouldReturnTopFilm() {
        Film film1 = inMemoryFilmStorage.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        Film film2 = inMemoryFilmStorage.createFilm(new Film(2, "Голодные игры", "Фильм про выживание", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        Film film3 = inMemoryFilmStorage.createFilm(new Film(3, "Иллюзия обмана2", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        Film film4 = inMemoryFilmStorage.createFilm(new Film(4, "Угнать за 60 сек", "Фильм про машины", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        Film film5 = inMemoryFilmStorage.createFilm(new Film(5, "Гарри Поттер", "Фильм про магию", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));

        User user = inMemoryUserStorage.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25), new HashSet<>()));
        User user2 = inMemoryUserStorage.createUser(new User(2, "a.ivanod@mail.ru", "ivan777", "", LocalDate.of(2000, 6, 25), new HashSet<>()));
        User user3 = inMemoryUserStorage.createUser(new User(3, "a.petrov@mail.ru", "petya111", "", LocalDate.of(2005, 6, 25), new HashSet<>()));

        filmService.addLike(film1.getId(), user.getId());//1 фильм 1 лайка

        filmService.addLike(film2.getId(), user.getId());
        filmService.addLike(film2.getId(), user2.getId());// 2 фильм 2 лайка

        filmService.addLike(film3.getId(), user.getId()); // 3 фильм 1 лайк

        filmService.addLike(film4.getId(), user.getId());
        filmService.addLike(film4.getId(), user2.getId());// 4 фильм 2 лайка

        filmService.addLike(film5.getId(), user.getId());
        filmService.addLike(film5.getId(), user2.getId());
        filmService.addLike(film5.getId(), user3.getId());// 5 фильм 3 лайка

        List<Film> topFilms = filmService.getTopFilms(3);
        assertEquals(3, topFilms.size());
        assertEquals("Гарри Поттер", topFilms.get(0).getName());
    }

    @Test
    public void shouldNotAddLike() {
        Film film = inMemoryFilmStorage.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        User user = inMemoryUserStorage.createUser(new User(5, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25), new HashSet<>()));

        assertEquals(0, film.getAllLikes().size());

        assertThrows(ObjectNotExistException.class, () -> filmService.addLike(2, 5));
    }

    @Test
    public void shouldNotDeleteLike() {
        Film film = inMemoryFilmStorage.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120, new HashSet<>()));
        User user = inMemoryUserStorage.createUser(new User(5, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25), new HashSet<>()));

        assertEquals(0, film.getAllLikes().size());

        filmService.addLike(1, 5);
        assertEquals(1, film.getAllLikes().size());
        assertTrue(film.getAllLikes().contains(5));

        assertThrows(ObjectNotExistException.class, () -> filmService.deleteLike(2, 5));
    }
}
