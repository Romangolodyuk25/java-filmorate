package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExist;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        log.debug("Количесвто пользователей в хранилище: " + users.size());
        return users.values();
    }

    @Override
    public User createUser(User user) {
        if (users.containsKey(user.getId())) {
            log.debug("Юзер с таким айди уже существует");
            throw new ObjectAlreadyExist("Данный айди уже существует");
        }
        users.put(user.getId(), user);
        log.debug("Пользователь " + user + " добавлен в хранилище");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        users.put(user.getId(), user);
        log.debug("Пользователь " + user + " обновлен");
        return user;
    }

    @Override
    public User getUserById(int id) {
        User receivedUser = users.get(id);
        if (receivedUser == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        log.debug("Пользователь " + receivedUser + " получен");
        return receivedUser;
    }

    @Override
    public void deleteUserById(int id) {
        User receivedUser = users.get(id);
        if (receivedUser == null) {
            log.debug("Пользователь с таким id не существует");
            throw new ObjectNotExistException("Некорректно введены данные");
        }
        users.remove(id);
        log.debug("Пользователь с id " + id + " удален");
    }

    @Override
    public void deleteAllUser() {
        users.clear();
    }

}
