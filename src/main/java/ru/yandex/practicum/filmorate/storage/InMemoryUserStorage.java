package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @Override
    public Collection<User> getAllUsers() {
        log.debug("Количесвто пользователей в хранилище: " + users.size());
        return users.values();
    }

    @Override
    public User createUser(User user) {
        checkValidation(user);
        user.setId(id);
        users.put(id, user);
        id++;
        log.debug("Пользователь " + user + " добавлен в хранилище");
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkValidation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.debug("Пользователь с таким id не существует");
            throw new ValidationException("Некорректно введены данные");
        }
        log.debug("Пользователь " + user + " обновлен");
        return user;
    }

    @Override
    public User getUserById(int id) {
        User receivedUser = users.get(id);
        if (id < 1) {
            log.debug("Некорректно введены данные");
            throw new ValidationException("Некорректно введены данные");
        }
        log.debug("Пользователь " + receivedUser + " получен");
        return receivedUser;
    }

    @Override
    public void deleteUserById(int id) {
        User receivedUser = users.get(id);
        if (id < 1) {
            log.debug("Некорректно введены данные");
            throw new ValidationException("Некорректно введены данные");
        }
        users.remove(id);
        log.debug("Пользователь с id " + id + " удален");
    }

    @Override
    public void deleteAllUser() {
        users.clear();
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
