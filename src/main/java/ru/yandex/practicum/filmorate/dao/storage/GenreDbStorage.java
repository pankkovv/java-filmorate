package ru.yandex.practicum.filmorate.dao.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("genre")
public class GenreDbStorage implements GenreDao {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findGenre() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }

    @Override
    public Optional<Genre> findGenreId(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", id);
        if (filmRows.next()) {
            Genre genre = new Genre(
                    filmRows.getInt("id"),
                    filmRows.getString("name")
            );

            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());

            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundException("Жанр с идентификатором " + id + " не найден.");
        }
    }
}
