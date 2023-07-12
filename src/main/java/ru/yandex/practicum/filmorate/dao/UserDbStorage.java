package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public class UserDbStorage implements UserStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * " +
                     "FROM USERS " +
                     "ORDER BY user_id ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO USERS (email, login, name, birthday) " +
                     "VALUES (?, ?, ?, ?) ";
        int userId = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return new User(userId, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? " +
                     "WHERE id = ? ";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT user_id, email, login, name, birthday " +
                     "FROM USERS WHERE user_id = ? ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id).get(id);
    }

    @Override
    public void deleteUserById(int id) {
        String sql = "DELETE FROM USERS " +
                     "WHERE id = ? ";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteAllUser() {
        String sql = "TRUNCATE TABLE USERS ";
        jdbcTemplate.update(sql);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(userId, email, login, name, birthday);
    }
}
