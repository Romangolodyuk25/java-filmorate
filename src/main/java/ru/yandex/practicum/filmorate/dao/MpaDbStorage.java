package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
public class MpaDbStorage implements MpaDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findRatingById(int id) {
        String sql = "SELECT mpa_id, name AS mpa_name " +
                "FROM MPA " +
                "WHERE mpa_id = ? ";
        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id);
        if (mpas.size() != 1) {
            return null;
        }
        return mpas.get(0);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * " +
                "FROM MPA " +
                "ORDER BY mpa_id ";
        List<Mpa> mpa = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
        if (mpa.size() == 0) {
            log.info("В таблице MPA нет жанров");
            return null;
        }
        return mpa;
    }

    @Override
    public void saveRating(Mpa mpa) {
        String sql = "INSERT INTO MPA (name) " +
                "VALUES (?) ";
        jdbcTemplate.update(sql, mpa.getName());
    }

    public Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
    }
}
