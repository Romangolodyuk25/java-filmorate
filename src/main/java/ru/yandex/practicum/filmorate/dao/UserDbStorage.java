package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Primary
@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * " +
                "FROM USERS " +
                "ORDER BY user_id ";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        if (users.size() == 0) {
            log.info("Таблица USERS пустая");
        }
        return users;
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?) ";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        log.info("Юзер " + user + " создан");
        return new User(user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getStatusFriend());
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ? ";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Юзер " + user + " Обновлен");
        return user;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT user_id, email, login, name, birthday " +
                "FROM USERS " +
                "WHERE user_id = ? ";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        if (users.size() != 1) {
            throw new ObjectNotExistException("Юзера не существует");
        }
        return users.get(0);
    }

    @Override
    public void deleteUserById(int id) {
        String sql = "DELETE FROM USERS " +
                "WHERE user_id = ? ";
        getUserById(id);
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAllUser() {
        String sql = "DELETE FROM USERS ";
        jdbcTemplate.update(sql);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "INSERT INTO FRIENDS (user_id, friend_id) " +
                "VALUES (?, ?) ";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDS " +
                "WHERE user_id = ? AND friend_id = ? ";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(int userId) {
        String sql = "SELECT u.* FROM friends f " +
                "JOIN users u ON f.friend_id = u.user_id " +
                "WHERE f.user_id = ? ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        String sql = "SELECT * " +
                "FROM USERS u " +
                "WHERE user_id in (SELECT f.friend_id FROM friends f WHERE f.user_id = ? AND f.friend_id in (SELECT f2.friend_id FROM friends f2 WHERE f2.user_id = ?))";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, otherId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                new HashMap<>()
        );
    }
}
