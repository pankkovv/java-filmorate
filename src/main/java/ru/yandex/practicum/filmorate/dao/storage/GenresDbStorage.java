package ru.yandex.practicum.filmorate.dao.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.GenreDao;
import ru.yandex.practicum.filmorate.dao.methods.GenresDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component("genres")
public class GenresDbStorage implements GenresDao {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> findGenresFilmId(long id) {
        String sql = "select * from genres where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenresId(rs), id);
    }

    private Genre makeGenresId(ResultSet rs) throws SQLException {
        return genreDao.findGenreId(rs.getInt("genre_id")).get();
    }

    @Override
    public void addGenresFilm(long filmId, List<Genre> listGenresFilm) {
        Set<Genre> setGenre = new TreeSet<>(this::compare);
        setGenre.addAll(listGenresFilm);
        for (Genre g : setGenre) {
            int filmAdd = jdbcTemplate.update("INSERT INTO genres (film_id, genre_id) VALUES (?,?)", filmId, g.getId());
        }
    }

    @Override
    public void removeGenresFilm(long filmId) {
        int genresRemove = jdbcTemplate.update("DELETE genres WHERE film_id = ?", filmId);
    }

    int compare(Genre p0, Genre p1) {
        if (p0.getId() > p1.getId()) {
            return 1;
        } else if (p0.getId() == p1.getId()) {
            return 0;
        } else {
            return -1;
        }
    }
}
