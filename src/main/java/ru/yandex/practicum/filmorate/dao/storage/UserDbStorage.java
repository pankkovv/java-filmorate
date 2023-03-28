package ru.yandex.practicum.filmorate.dao.storage;

import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("user")
public class UserDbStorage implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private long localInt = 1;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findUser() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs));
    }

    @Override
    public Optional<User> findUserId(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);
        if (userRows.next()) {
            User user = User.builder().id(userRows.getInt("id")).email(userRows.getString("email")).login(userRows.getString("login")).name(userRows.getString("name")).birthday(userRows.getDate("birthday").toLocalDate()).build();

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);

            throw new NotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public Optional<User> createUser(User user) {
        try {
            validate(user);
            user.setId(localInt);
            localInt++;

            int userAdd = jdbcTemplate.update("INSERT INTO users (id, login, name, email, birthday) VALUES (?,?,?,?,?)", user.getId(), user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());

            return findUserId(user.getId());
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        try {
            validate(user);
            findUserId(user.getId());

            int userUpdate = jdbcTemplate.update("UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?", user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());

            return findUserId(user.getId());
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private User makeUsers(ResultSet rs) throws SQLException {
        return User.builder().id(rs.getInt("id")).email(rs.getString("email")).login(rs.getString("login")).name(rs.getString("name")).birthday(rs.getDate("birthday").toLocalDate()).build();
    }

    private void validate(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
