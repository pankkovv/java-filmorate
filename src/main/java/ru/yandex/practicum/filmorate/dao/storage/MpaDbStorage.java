package ru.yandex.practicum.filmorate.dao.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("mpa")
public class MpaDbStorage implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findMpa() {
        String sql = "select * from mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return Mpa.builder().id(rs.getInt("id")).name(rs.getString("name")).build();
    }

    @Override
    public Optional<Mpa> findMpaId(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from mpa where id = ?", id);
        if (filmRows.next()) {
            Mpa mpa = Mpa.builder().id(filmRows.getInt("id")).name(filmRows.getString("name")).build();

            log.info("Найден рейтинг: {} {}", mpa.getId(), mpa.getName());

            return Optional.of(mpa);
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", id);

            throw new NotFoundException("Рейтинг с идентификатором " + id + " не найден.");
        }
    }
}
