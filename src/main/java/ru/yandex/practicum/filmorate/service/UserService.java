package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service

public class UserService {

    @Qualifier("UserDbStorage")
    private final UserStorage userDbStorage;
    @Qualifier("FilmDbStorage")
    private final FilmStorage filmDbStorage;
    private int id = 1;

    @Autowired
    public UserService(UserStorage userDbStorage, FilmStorage filmDbStorage) {
        this.userDbStorage = userDbStorage;
        this.filmDbStorage = filmDbStorage;
    }

    public User createUser(User user) {
        checkValidation(user);
        user.setId(id);
        id++;
        userDbStorage.createUser(user);
        return user;
    }

    public void deleteUserById(int id) {
        User receivedUser = userDbStorage.getUserById(id);
        if (receivedUser == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        userDbStorage.deleteUserById(id);
    }

    public User updateUser(User user) {
        checkValidation(user);
        if (userDbStorage.getUserById(user.getId()) == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        userDbStorage.updateUser(user);
        return user;
    }

    public User getUserById(int id) {
        User receivedUser = userDbStorage.getUserById(id);
        if (receivedUser == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        return receivedUser;
    }

    public Collection<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        User receivedUser = userDbStorage.getUserById(userId);
        User receivedFriend = userDbStorage.getUserById(friendId);
        receivedUser.addFriend(friendId);
        userDbStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        User receivedUser = userDbStorage.getUserById(userId);
        User receivedFriend = userDbStorage.getUserById(friendId);
        receivedUser.deleteFriend(friendId);
        userDbStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends(int userId) {
        User receivedUser = userDbStorage.getUserById(userId);
        return userDbStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User receivedUser = userDbStorage.getUserById(userId);
        User receivedFriend = userDbStorage.getUserById(otherId);
        if (receivedUser == null || receivedFriend == null) {
            log.debug("Некорректно передан айди юзера или друга");
            throw new ObjectNotExistException("Переданный айди не найден");
        }
        return userDbStorage.getCommonFriends(userId, otherId);
    }

    public void deleteAllUsers() {
        userDbStorage.deleteAllUser();
        id = 1;
    }

    public void checkValidation(User user) {
        if (user.getEmail() == null || user.getLogin() == null || user.getBirthday() == null ||
                user.getEmail().isEmpty() || !user.getEmail().contains("@") || user.getLogin().isEmpty() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Данный объект содержит некорректные данные: " + user);
            throw new ValidationException("Некорректно введены данные");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
