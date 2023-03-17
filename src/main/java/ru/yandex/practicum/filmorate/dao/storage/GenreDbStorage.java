package ru.yandex.practicum.filmorate.dao.storage;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component("genre")
public class GenreDbStorage implements GenreDao {

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
        return Genre.builder().id(rs.getInt("id")).name(rs.getString("name")).build();
    }

    @Override
    public Optional<Genre> findGenreId(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", id);
        if (filmRows.next()) {
            Genre genre = Genre.builder().id(filmRows.getInt("id")).name(filmRows.getString("name")).build();

            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());

            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);

            throw new NotFoundException("Жанр с идентификатором " + id + " не найден.");
        }
    }
}
