package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    void deleteUserById(int id);

    void deleteAllUser();

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<User> getAllFriends(int userId);

    List<User> getCommonFriends(int userId, int otherId);
}
