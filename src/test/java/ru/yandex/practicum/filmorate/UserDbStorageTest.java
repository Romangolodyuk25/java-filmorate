package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(UserDbStorage.class)
public class UserDbStorageTest {

    @Autowired
    private UserStorage userStorage;

    @Test
    public void testFindUserById() {
        User user = userStorage.getUserById(1);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("email@mail.ru", user.getEmail());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = (List<User>) userStorage.getAllUsers();

        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);

        assertEquals(1, user1.getId());
        assertEquals(2, user2.getId());
        assertEquals(3, user3.getId());

        assertEquals("email@mail.ru", user1.getEmail());
        assertEquals("email2@mail.ru", user2.getEmail());
        assertEquals("email3@mail.ru", user3.getEmail());
    }

    @Test
    public void testDeleteById() {
        User user = userStorage.getUserById(1);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("email@mail.ru", user.getEmail());

        userStorage.deleteUserById(1);

        assertThrows(ObjectNotExistException.class, () -> userStorage.getUserById(1));

    }

    @Test
    public void deleteAllUsers() {
        List<User> users = (List<User>) userStorage.getAllUsers();
        User user1 = users.get(0);
        User user2 = users.get(1);
        User user3 = users.get(2);

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);

        assertEquals(1, user1.getId());
        assertEquals(2, user2.getId());
        assertEquals(3, user3.getId());

        assertEquals("email@mail.ru", user1.getEmail());
        assertEquals("email2@mail.ru", user2.getEmail());
        assertEquals("email3@mail.ru", user3.getEmail());

        userStorage.deleteAllUser();

        assertThrows(ObjectNotExistException.class, () -> userStorage.getUserById(1));
        assertThrows(ObjectNotExistException.class, () -> userStorage.getUserById(2));
        assertThrows(ObjectNotExistException.class, () -> userStorage.getUserById(3));

    }


}
