package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

@SpringBootTest
public class UserServiceTest {
    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @BeforeEach
    public void beforeEach() {
        userService.deleteAllUsers();
    }

    @Test
    public void shouldAddFriend() {
        User user = userService.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25)));
        User user2 = userService.createUser(new User(2, "a.ivanod@mail.ru", "ivan777", "", LocalDate.of(2000, 6, 25)));
        assertEquals(0, user.getFriends().size());
        assertEquals(0, user2.getFriends().size());

        userService.addFriend(1, 2);
        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());
    }

    @Test
    public void shouldDeleteAddFriend() {
        User user = userService.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25)));
        User user2 = userService.createUser(new User(2, "a.ivanod@mail.ru", "ivan777", "", LocalDate.of(2000, 6, 25)));
        assertEquals(0, user.getFriends().size());
        assertEquals(0, user2.getFriends().size());

        userService.addFriend(1, 2);
        assertEquals(1, user.getFriends().size());
        assertEquals(1, user2.getFriends().size());

        userService.deleteFriend(1, 2);
        assertEquals(0, user.getFriends().size());
        assertEquals(0, user2.getFriends().size());
    }

    @Test
    public void shouldGetFriends() {
        User user = userService.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25)));
        User user2 = userService.createUser(new User(2, "a.ivanod@mail.ru", "ivan777", "", LocalDate.of(2000, 6, 25)));
        User user3 = userService.createUser(new User(3, "a.petrov@mail.ru", "petya111", "", LocalDate.of(2005, 6, 25)));

        assertEquals(0, user.getFriends().size());

        userService.addFriend(1, 2);
        userService.addFriend(1, 3);

        assertEquals(2, user.getFriends().size());
    }

    @Test
    public void shouldGetCommonFriends() {
        User user = userService.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25)));
        User user2 = userService.createUser(new User(2, "a.ivanod@mail.ru", "ivan777", "", LocalDate.of(2000, 6, 25)));
        User user3 = userService.createUser(new User(3, "a.petrov@mail.ru", "petya111", "", LocalDate.of(2005, 6, 25)));

        assertEquals(0, userService.getCommonFriends(1, 2).size());

        userService.addFriend(1, 3);
        userService.addFriend(2, 3);

        assertEquals(1, userService.getCommonFriends(1, 2).size());
        assertEquals(3, userService.getCommonFriends(1, 2).get(0).getId());
    }

    @Test
    public void shouldNotAddFriendIfUserNotExist() {
        User user = userService.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25)));
        User user2 = userService.createUser(new User(2, "a.ivanod@mail.ru", "ivan777", "", LocalDate.of(2000, 6, 25)));

        assertEquals(0, userService.getCommonFriends(1, 2).size());

        assertThrows(ObjectNotExistException.class, () -> userService.addFriend(1, 3));
    }

    @Test
    public void shouldNotGetAllFriendFroNotExistUser() {
        User user = userService.createUser(new User(1, "r.golodyuk@mail.ru", "Roman123", "", LocalDate.of(1999, 6, 25)));
        User user2 = userService.createUser(new User(2, "a.ivanod@mail.ru", "ivan777", "", LocalDate.of(2000, 6, 25)));

        userService.addFriend(1, 2);

        assertEquals(1, userService.getAllFriends(1).size());
        assertThrows(ObjectNotExistException.class, () -> userService.getAllFriends(3));
    }
}
