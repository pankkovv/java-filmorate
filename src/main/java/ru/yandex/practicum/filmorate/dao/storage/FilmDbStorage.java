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
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

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
    public Optional<Film> findFilmId(long id) throws NotFoundException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate"),
                    mpaDao.findMpaId(filmRows.getInt("mpa_id")).get(),
                    genresDao.findGenresFilmId(filmRows.getInt("id"))
            );

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

            int filmAdd = jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, rate, mpa_id) VALUES (?,?,?,?,?,?)",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getRate(),
                    film.getMpa().getId());

            if (!film.getGenres().isEmpty()) {
                for (Genre g : film.getGenres()) {
                    genresDao.addGenresFilm(film.getId(), g.getId());
                }
            }

            return findFilmId(film.getId());
        } catch (DataAccessException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) throws ValidationException {
        try {
            validate(film);
            findFilmId(film.getId());

            if (film.getGenres().isEmpty()) {
                genresDao.removeGenresFilm(film.getId());
            } else {
                genresDao.removeGenresFilm(film.getId());
                Set<Genre> setGenre = new TreeSet<>(this::compare);
                setGenre.addAll(film.getGenres());
                for (Genre g : setGenre) {
                    genresDao.addGenresFilm(film.getId(), g.getId());
                }
            }

            int filmUpdate = jdbcTemplate.update("UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? WHERE id = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getRate(),
                    film.getMpa().getId(),
                    film.getId()
            );

            return findFilmId(film.getId());
        } catch (DataAccessException e) {
            throw new ValidationException();
        }
    }

    private Film makeFilms(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");
        Integer rate = rs.getInt("rate");
        Mpa mpa = mpaDao.findMpaId(rs.getInt("mpa_id")).get();
        List<Genre> genres = genresDao.findGenresFilmId(rs.getInt("id"));

        return new Film(id, name, description, releaseDate, duration, rate, mpa, genres);
    }

    void validate(Film film) {
        if (film.getGenres() == null) {
            film.setGenres(List.of());
        }
        if (film.getRate() == null) {
            film.setRate(0);
        }
    }

    int compare(Genre p0, Genre p1) {
        if(p0.getId() > p1.getId()){
            return 1;
        } else if (p0.getId() == p1.getId()){
            return 0;
        } else {
            return -1;
        }
    }
}
