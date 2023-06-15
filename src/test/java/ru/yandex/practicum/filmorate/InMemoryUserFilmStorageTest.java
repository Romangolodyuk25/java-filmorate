package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class InMemoryUserFilmStorageTest {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public InMemoryUserFilmStorageTest(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @BeforeEach
    public void beforeEach() {
        inMemoryUserStorage.deleteAllUser();
    }

    @Test
    public void shouldCreateUser() {
        assertEquals(0, inMemoryUserStorage.getAllUsers().size());

        inMemoryUserStorage.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "roma", LocalDate.of(1999, 6, 25), Set.of(1, 2, 3)));
        assertEquals(1, inMemoryUserStorage.getAllUsers().size());
    }

    @Test
    public void shouldNotCreateEmptyUser() {
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User(0, "", "", "", null, Set.of(1, 2, 3))));
        assertEquals(0, inMemoryUserStorage.getAllUsers().size());
    }

    @Test
    public void shouldNotCreateUserWithEmptyEmail() {
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User(1, "", "Roman123", "roma", LocalDate.of(1999, 6, 25), Set.of(1, 2, 3))));
        assertEquals(0, inMemoryUserStorage.getAllUsers().size());
    }

    @Test
    public void shouldNotCreateUserIfEmailNotHaveSymbol() {
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User(1, "r.golodyuk.ru", "Roman123", "roma", LocalDate.of(1999, 6, 25), Set.of(1, 2, 3))));
        assertEquals(0, inMemoryUserStorage.getAllUsers().size());
    }

    @Test
    public void shouldNotCreateUserWithEmptyLogin() {
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User(1, "r.golodyuk@mail.ru", "", "roma", LocalDate.of(1999, 6, 25), Set.of(1, 2, 3))));
        assertEquals(0, inMemoryUserStorage.getAllUsers().size());
    }

    @Test
    public void shouldNotCreateUserWithBlankLogin() {
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User(1, "r.golodyuk@mail.ru", "Roman 123", "roma", LocalDate.of(1999, 6, 25), Set.of(1, 2, 3))));
        assertEquals(0, inMemoryUserStorage.getAllUsers().size());
    }

    @Test
    public void shouldCreateUserWithEmptyName() {
        User user = inMemoryUserStorage.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25), Set.of(1, 2, 3)));
        assertEquals(1, inMemoryUserStorage.getAllUsers().size());
        assertEquals("Roman123", user.getName());
    }

    @Test
    public void shouldNotCreateUserIfBirthdayNotCorrect() {
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "roma", LocalDate.of(2024, 6, 25), Set.of(1, 2, 3))));
        assertEquals(0, inMemoryUserStorage.getAllUsers().size());
    }
}
