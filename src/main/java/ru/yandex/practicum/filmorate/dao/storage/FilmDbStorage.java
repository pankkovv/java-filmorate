package ru.yandex.practicum.filmorate.dao.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.FilmDao;
import ru.yandex.practicum.filmorate.dao.methods.GenresDao;
import ru.yandex.practicum.filmorate.dao.methods.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("film")
public class FilmDbStorage implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private MpaDao mpaDao;
    private GenresDao genresDao;
    private Integer localId = 1;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenresDao genresDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genresDao = genresDao;
    }

    @Override
    public List<Film> findFilm() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilms(rs));
    }

    @Override
    public Optional<Film> findFilmId(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        if (filmRows.next()) {
            Film film = Film.builder().id(filmRows.getInt("id")).name(filmRows.getString("name")).description(filmRows.getString("description")).releaseDate(filmRows.getDate("release_date").toLocalDate()).duration(filmRows.getInt("duration")).rate(filmRows.getInt("rate")).mpa(mpaDao.findMpaId(filmRows.getInt("mpa_id")).get()).genres(genresDao.findGenresFilmId(filmRows.getInt("id"))).build();

            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);

            throw new NotFoundException("Фильм с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public List<Film> findPopularFilm(Integer count) {
        String sql = "select * from films group by name order by rate desc limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilms(rs), count);
    }

    @Override
    public Optional<Film> createFilm(Film film) {
        try {
            validate(film);
            film.setId(localId);
            localId++;

            int filmAdd = jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, rate, mpa_id) VALUES (?,?,?,?,?,?)", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId());

            if (!film.getGenres().isEmpty()) {
                genresDao.addGenresFilm(film.getId(), film.getGenres());
            }

            return findFilmId(film.getId());
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        try {
            validate(film);
            findFilmId(film.getId());

            if (film.getGenres().isEmpty()) {
                genresDao.removeGenresFilm(film.getId());
            } else {
                genresDao.removeGenresFilm(film.getId());
                genresDao.addGenresFilm(film.getId(), film.getGenres());
            }

            int filmUpdate = jdbcTemplate.update("UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? WHERE id = ?", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());

            return findFilmId(film.getId());
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private Film makeFilms(ResultSet rs) throws SQLException {
        return Film.builder().id(rs.getInt("id")).name(rs.getString("name")).description(rs.getString("description")).releaseDate(rs.getDate("release_date").toLocalDate()).duration(rs.getInt("duration")).rate(rs.getInt("rate")).mpa(mpaDao.findMpaId(rs.getInt("mpa_id")).get()).genres(genresDao.findGenresFilmId(rs.getInt("id"))).build();
    }

    void validate(Film film) {
        if (film.getGenres() == null) {
            film.setGenres(List.of());
        }
        if (film.getRate() == null) {
            film.setRate(0);
        }
    }
}
