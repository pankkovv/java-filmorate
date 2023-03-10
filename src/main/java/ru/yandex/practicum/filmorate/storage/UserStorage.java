package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserStorage {
    Set<User> findUser();
    User findUserId(Integer id);
    User createUser(User user) throws ValidationException;
    User updateUser(User user) throws ValidationException;
}
