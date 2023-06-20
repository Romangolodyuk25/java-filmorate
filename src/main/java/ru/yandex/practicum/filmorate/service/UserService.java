package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage inMemoryUserStorage;
    private int id = 1;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User createUser(User user) {
        checkValidation(user);
        user.setId(id);
        id++;
        inMemoryUserStorage.createUser(user);
        return user;
    }

    public void deleteUserById(int id) {
        User receivedUser = inMemoryUserStorage.getUserById(id);
        if (receivedUser == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        inMemoryUserStorage.deleteUserById(id);
    }

    public User updateUser(User user) {
        checkValidation(user);
        if (inMemoryUserStorage.getUserById(user.getId()) == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        inMemoryUserStorage.updateUser(user);
        return user;
    }

    public User getUserById(int id) {
        User receivedUser = inMemoryUserStorage.getUserById(id);
        if (receivedUser == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        return receivedUser;
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        User receivedFriend = inMemoryUserStorage.getUserById(friendId);
        receivedUser.addFriend(friendId);
        receivedFriend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        User receivedFriend = inMemoryUserStorage.getUserById(friendId);
        receivedUser.deleteFriend(friendId);
        receivedFriend.deleteFriend(userId);
    }

    public List<User> getAllFriends(int userId) {
        List<User> friends = new ArrayList<>();
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        for (Integer id : receivedUser.getFriends()) {
            friends.add(inMemoryUserStorage.getUserById(id));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        User receivedUser = inMemoryUserStorage.getUserById(userId);
        User receivedFriend = inMemoryUserStorage.getUserById(otherId);
        if (receivedUser == null || receivedFriend == null || userId < 0 || otherId < 0) {
            log.debug("Некорректно передан айди юзера или друга");
            throw new ObjectNotExistException("Переданный айди не найден");
        }
        if (receivedUser.getFriends() != null && !receivedUser.getFriends().isEmpty()) {
            for (Integer id : receivedUser.getFriends()) {
                if (receivedFriend.getFriends() != null && !receivedFriend.getFriends().isEmpty()) {
                    if (receivedFriend.getFriends().contains(id)) {
                        commonFriends.add(inMemoryUserStorage.getUserById(id));
                    }
                }
            }
        }
        return commonFriends;
    }

    public void deleteAllUsers() {
        inMemoryUserStorage.deleteAllUser();
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
