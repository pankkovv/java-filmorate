package ru.yandex.practicum.filmorate.dao.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component("user")
public class UserDbStorage implements UserDao {

    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private int localInt = 1;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findUser() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs));
    }

    @Override
    public Optional<User> findUserId(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);

            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public Optional<User> createUser(User user) throws ValidationException {
        try {
            validate(user);
            user.setId(localInt);
            localInt++;

            int userAdd = jdbcTemplate.update("INSERT INTO users (id, login, name, email, birthday) VALUES (?,?,?,?,?)",
                    user.getId(),
                    user.getLogin(),
                    user.getName(),
                    user.getEmail(),
                    user.getBirthday());

            return findUserId(user.getId());
        } catch (DataAccessException e) {
            throw new ValidationException();
        }
    }

    @Override
    public Optional<User> updateUser(User user) throws ValidationException {
        try {
            validate(user);
            findUserId(user.getId());

            int userUpdate = jdbcTemplate.update("UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?",
                    user.getLogin(),
                    user.getName(),
                    user.getEmail(),
                    user.getBirthday(),
                    user.getId());

            return findUserId(user.getId());
        } catch (DataAccessException e) {
            throw new ValidationException();
        }
    }

    private User makeUsers(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

    private void validate(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
