package ru.yandex.practicum.filmorate.dao.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.methods.FriendsDao;
import ru.yandex.practicum.filmorate.dao.methods.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component("friends")
public class FriendsDbService implements FriendsDao {

    private final Logger log = LoggerFactory.getLogger(FriendsDbService.class);
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    public FriendsDbService(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public List<User> findFriendsUserId(Integer id) {
        String sql = "select * from users where id in (select friend_id from friends where user_id = ? and status_id = 1 group by friend_id)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUsers(rs), id);
    }

    @Override
    public List<User> findCommonFriends(Integer id, Integer otherId) {
        String sql = "select * from users where id in (select friend_id from friends where (user_id = ? or user_id = ?) and status_id = 1 and (friend_id != ? and friend_id != ?) group by friend_id)";
        return jdbcTemplate.query(sql, new Object[]{id, otherId, id, otherId}, (rs, rowNum) -> makeUsers(rs));
    }

    private User makeUsers(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

    @Override
    public List<User> addFriend(Integer userOneId, Integer userTwoId) throws Throwable {
        try {
            if (!validateExists(userOneId, userTwoId)) {
                int friendAdd = jdbcTemplate.update("INSERT INTO friends (user_id, status_id, friend_id) VALUES (?,?,?)",
                        userDao.findUserId(userOneId).get().getId(),
                        1,
                        userDao.findUserId(userTwoId).get().getId());

                return findFriendsUserId(userOneId);
            }
        } catch (DataAccessException e) {
            throw new Throwable();
        }
        return List.of();
    }

    @Override
    public List<User> deleteFriend(Integer userOneId, Integer userTwoId) throws Throwable {
        try {
            if (validateExists(userOneId, userTwoId)) {
                int friendAdd = jdbcTemplate.update("DELETE friends WHERE user_id = ? AND friend_id = ?",
                        userDao.findUserId(userOneId).get().getId(),
                        userDao.findUserId(userTwoId).get().getId());

                return findFriendsUserId(userOneId);
            }
        } catch (DataAccessException e) {
            throw new Throwable();
        }
        return List.of();
    }

    private boolean validateExists(Integer userOneId, Integer userTwoId) throws NotFoundException {
        if (findFriendsUserId(userOneId).contains(userDao.findUserId(userTwoId).get())) {
            return true;
        } else {
            return false;
        }
    }
}
