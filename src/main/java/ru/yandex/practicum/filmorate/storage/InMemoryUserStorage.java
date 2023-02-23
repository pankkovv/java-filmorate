package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Set<User> users = new HashSet<>();
    private int id = 1;

    @Override
    public Set<User> findUser() {
        log.debug("Количество пользователей:{}", users.size());
        if (users.isEmpty()) {
            return Set.of();
        }
        return users;
    }

    @Override
    public User findUserId(Integer id) {
        return users.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", id)));
    }

    @Override
    public User createUser(User user) throws ValidationException {
        log.debug("Получен запрос POST /users.");

        validate(user);

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }

        user.setId(id);
        log.debug("Добавлен пользователь:{}", user);
        users.add(user);
        id++;
        return user;
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        log.debug("Получен запрос PUT /users.");

        validate(user);

        for (User i : users) {
            if (i.getId() == user.getId()) {
                i.setId(user.getId());
                i.setName(user.getName());
                i.setLogin(user.getLogin());
                i.setEmail(user.getEmail());
                i.setBirthday(user.getBirthday());
            } else {
                throw new NotFoundException(String.format("Пользователя № %d не существует.", user.getId()));
            }
        }

        log.debug("Пользователь обновлен:{}", user);
        return user;
    }

    private void validate(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
