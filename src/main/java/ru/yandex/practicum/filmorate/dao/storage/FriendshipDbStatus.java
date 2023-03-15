package ru.yandex.practicum.filmorate.dao.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.FriendshipStatusDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("frStatus")
public class FriendshipDbStatus implements FriendshipStatusDao {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FriendshipDbStatus(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<FriendshipStatus> findStatus() {
        String sql = "select * from friendship_status";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeStatus(rs));
    }

    private FriendshipStatus makeStatus(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Boolean name = rs.getBoolean("name");

        return new FriendshipStatus(id, name);
    }

    @Override
    public Optional<FriendshipStatus> findStatusId(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from friendship_status where id = ?", id);
        if (filmRows.next()) {
            FriendshipStatus status = new FriendshipStatus(
                    filmRows.getInt("id"),
                    filmRows.getBoolean("name")
            );

            log.info("Статус {} определен: {}", status.getId(), status.getName());

            return Optional.of(status);
        } else {
            log.info("Статус с идентификатором {} не найден.", id);

            throw new NotFoundException("Статус с идентификатором " + id + " не найден.");
        }
    }
}
