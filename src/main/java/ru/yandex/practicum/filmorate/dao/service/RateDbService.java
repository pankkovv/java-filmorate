package ru.yandex.practicum.filmorate.dao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.FilmDao;
import ru.yandex.practicum.filmorate.dao.methods.RateDao;
import ru.yandex.practicum.filmorate.dao.methods.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("rate")
public class RateDbService implements RateDao {
    private final Logger log = LoggerFactory.getLogger(RateDbService.class);
    private final JdbcTemplate jdbcTemplate;
    private FilmDao filmDao;
    private UserDao userDao;

    @Autowired
    public RateDbService(JdbcTemplate jdbcTemplate, FilmDao filmDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDao = filmDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<Film> addLike(Integer filmId, Integer userId) throws Throwable {
        try {
            if (!validateExists(filmId, userId)) {
                int rateAdd = jdbcTemplate.update("INSERT INTO rate (film_id, user_id) VALUES (?,?)",
                        filmId,
                        userId
                );

                int filmRateAdd = jdbcTemplate.update("UPDATE films SET rate = rate + 1 WHERE id = ?",
                        filmId
                );
            }
            return filmDao.findFilmId(filmId);
        } catch (DataAccessException e) {
            throw new Throwable();
        }
    }

    @Override
    public Optional<Film> removeLike(Integer filmId, Integer userId) throws Throwable {
        try {
            if (validateExists(filmId, userId)) {
                int rateRemove = jdbcTemplate.update("DELETE FROM rate WHERE film_id = ? AND user_id = ?",
                        filmId,
                        userId
                );

                int filmRateRemove = jdbcTemplate.update("UPDATE films SET rate = rate - 1 WHERE id = ?",
                        filmId
                );
            }
            return filmDao.findFilmId(filmId);
        } catch (DataAccessException e) {
            throw new Throwable();
        }
    }

    public List<Integer> findUserRateFilm(Integer filmId) {
        String sql = "select user_id from rate where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), filmId);
    }

    private Integer makeUser(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("user_id");
        return id;
    }

    private boolean validateExists(Integer filmId, Integer userId) throws NotFoundException {
        if (findUserRateFilm(filmDao.findFilmId(filmId).get().getId()).contains(userDao.findUserId(userId).get().getId())) {
            return true;
        } else {
            return false;
        }
    }
}
