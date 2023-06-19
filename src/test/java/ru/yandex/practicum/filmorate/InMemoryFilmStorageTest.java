package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class InMemoryFilmStorageTest {
    @Autowired
    private FilmService filmService;// вызов из InMemoryFilmStorage

    @BeforeEach
    public void beforeEach() {
        filmService.deleteAllFilms();
    }

    @Test
    public void shouldCreateFilm() {
        filmService.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120));
        assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    public void shouldNotCreateFilmWithEmptyName() {
        assertThrows(ValidationException.class, () -> filmService.createFilm(new Film(1, "", "Фильм про фокусы", LocalDate.of(2010, 7, 20), 120)));
        assertEquals(0, filmService.getAllFilms().size());
    }

    @Test
    public void shouldNotCreateFilmIfLengthDescriptionNotCorrect() {
        assertThrows(ValidationException.class, () -> filmService.createFilm(new Film(1, "Иллюзия обмана", "Душа моя озарена неземной радостью, как эти чудесные весенние утра, которыми я наслаждаюсь от всего сердца. Я совсем один и блаженствую в здешнем краю, словно созданном для таких, как я. Я так счастлив, мой друг, так упоен ощущением покоя, что искусство мое страдает от этого. Ни одного штриха не мог бы я сделать, а никогда не был таким большим художником, как в эти минуты. Когда от милой моей долины поднимается пар и полдневное солнце стоит над непроницаемой чащей темного леса и лишь редкий луч проскальзывает в его святая святых, а я лежу в высокой траве у быстрого ручья и, прильнув к земле, вижу тысячи всевозможных былинок и чувствую, как близок моему сердцу крошечный мирок, что снует между стебельками, наблюдаю эти неисчислимые, непостижимые разновидности червяков и мошек и чувствую близость всемогущего, создавшего нас по своему подобию, веяние вселюбящего, судившего нам парить в вечном блаженстве, когда взор мой туманится и все вокруг меня и небо надо мной запечатлены в моей душе, точно образ возлюбленной, - тогда, дорогой друг, меня часто томит мысль: \"Ах! Как бы выразить, как бы вдохнуть в рисунок то, что так полно, так трепетно живет во мне, запечатлеть отражение моей души, как душа моя - отражение предвечного бога!\" Друг мой...",
                LocalDate.of(2010, 7, 20), 120)));
        assertEquals(0, filmService.getAllFilms().size());
    }

    @Test
    public void shouldNotCreateFilmIfReleaseDateNotCorrect() {
        assertThrows(ValidationException.class, () -> filmService.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(1894, 7, 20), 120)));
        assertEquals(0, filmService.getAllFilms().size());
    }

    @Test
    public void shouldCreateFilmIfReleaseDateCorrect() {
        filmService.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(1895, 7, 20), 120));
        assertEquals(1, filmService.getAllFilms().size());
    }

    @Test
    public void shouldNotCreateFilmIfDurationNegative() {
        assertThrows(ValidationException.class, () -> filmService.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(1894, 7, 20), -20)));
        assertEquals(0, filmService.getAllFilms().size());
    }

    @Test
    public void shouldNotCreateFilmIfDurationEqualsZero() {
        assertThrows(ValidationException.class, () -> filmService.createFilm(new Film(1, "Иллюзия обмана", "Фильм про фокусы", LocalDate.of(1894, 7, 20), 0)));
        assertEquals(0, filmService.getAllFilms().size());
    }

}
