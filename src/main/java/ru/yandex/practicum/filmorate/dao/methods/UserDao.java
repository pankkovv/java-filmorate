package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findUser();

    Optional<User> findUserId(long id);

    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);
}
