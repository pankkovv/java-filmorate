package ru.yandex.practicum.filmorate.dao.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.FilmDao;
import ru.yandex.practicum.filmorate.dao.methods.RateDao;
import ru.yandex.practicum.filmorate.dao.methods.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("rate")
public class RateDbStorage implements RateDao {
    private final JdbcTemplate jdbcTemplate;
    private FilmDao filmDao;
    private UserDao userDao;

    @Autowired
    public RateDbStorage(JdbcTemplate jdbcTemplate, FilmDao filmDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDao = filmDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<Film> addLike(long filmId, long userId) {
        try {
            if (!validateExists(filmId, userId)) {
                int rateAdd = jdbcTemplate.update("INSERT INTO rate (film_id, user_id) VALUES (?,?)", filmId, userId);
                int filmRateAdd = jdbcTemplate.update("UPDATE films SET rate = rate + 1 WHERE id = ?", filmId);
            }
            return filmDao.findFilmId(filmId);
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public Optional<Film> removeLike(long filmId, long userId) {
        try {
            if (validateExists(filmId, userId)) {
                int rateRemove = jdbcTemplate.update("DELETE FROM rate WHERE film_id = ? AND user_id = ?", filmId, userId);
                int filmRateRemove = jdbcTemplate.update("UPDATE films SET rate = rate - 1 WHERE id = ?", filmId);
            }
            return filmDao.findFilmId(filmId);
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public List<Long> findUserRateFilm(long filmId) {
        String sql = "select user_id from rate where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), filmId);
    }

    private long makeUser(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }

    private boolean validateExists(long filmId, long userId) throws NotFoundException {
        if (findUserRateFilm(filmDao.findFilmId(filmId).get().getId()).contains(userDao.findUserId(userId).get().getId())) {
            return true;
        } else {
            return false;
        }
    }
}
