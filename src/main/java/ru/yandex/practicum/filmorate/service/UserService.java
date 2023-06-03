package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Service
public class UserService {
    private final HashMap<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    public Collection<User> getAllUsers() {
        log.debug("Количесвто пользователей в хранилище: " + users.size());
        return users.values();
    }

    public User createUser(User user) {
        checkValidation(user);
        user.setId(id);
        users.put(id, user);
        id++;
        log.debug("Пользователь " + user.toString() + " добавлен в хранилище");
        return user;
    }

    public User updateUser(User user) {
        checkValidation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            log.debug("Пользователь с таким id не существует");
            throw new ValidationException("Некорректно введены данные");
        }
        log.debug("Пользователь " + user.toString() + " обновлен");
        return user;
    }

    public void clearUsers() {
        users.clear();
    }

    public void checkValidation(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") || user.getLogin().isEmpty() ||
                user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Данный объект содержит некорректные данные: " + user.toString());
            throw new ValidationException("Некорректно введены данные");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
