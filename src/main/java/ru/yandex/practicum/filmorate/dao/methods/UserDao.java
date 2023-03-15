package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findUser();

    Optional<User> findUserId(Integer id);

    Optional<User> createUser(User user) throws ValidationException;

    Optional<User> updateUser(User user) throws ValidationException;
}
