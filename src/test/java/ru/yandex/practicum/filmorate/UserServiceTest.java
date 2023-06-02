package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    public User createUser(){
        return new User(1, "roman.golodyuk@mail.ru", "Romchik01RUS", "krasauchik",LocalDate.of(1999,6,25));
    }

    @Test
    public void shouldCreateUser(){
        userService.createUser(createUser());
        assertEquals(1, userService.getAllUsers().size());

    }

    @Test
    public void shouldNotCreateEmptyUser(){
        assertThrows(ValidationException.class ,() -> userService.createUser(new User(0, "", "", "", null)));
    }
}
