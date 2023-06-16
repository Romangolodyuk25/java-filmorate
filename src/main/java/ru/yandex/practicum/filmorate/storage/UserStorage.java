package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    public Collection<User> getAllUsers();

    public User createUser(User user);

    public User updateUser(User user);

    public User getUserById(int id);

    public void deleteUserById(int id);

    public void deleteAllUser();

}
