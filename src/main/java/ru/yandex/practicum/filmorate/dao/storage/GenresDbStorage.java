package ru.yandex.practicum.filmorate.dao.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.GenreDao;
import ru.yandex.practicum.filmorate.dao.methods.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("genres")
public class GenresDbStorage implements GenresDao {
    private final Logger log = LoggerFactory.getLogger(GenreDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> findGenresFilmId(Integer id) {
        String sql = "select * from genres where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenresId(rs), id);
    }

    private Genre makeGenresId(ResultSet rs) throws SQLException {
        Integer genreId = rs.getInt("genre_id");
        return genreDao.findGenreId(genreId).get();
    }

    @Override
    public void addGenresFilm(Integer filmId, Integer genreId) {
        int filmAdd = jdbcTemplate.update("INSERT INTO genres (film_id, genre_id) VALUES (?,?)",
                filmId,
                genreId);
    }

    @Override
    public void removeGenresFilm(Integer filmId) {
        int friendAdd = jdbcTemplate.update("DELETE genres WHERE film_id = ?", filmId);
    }
}
