package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Set<User> users = new HashSet<>();
    private int id = 1;

    @GetMapping
    public Set<User> findUser() {
        log.debug("Количество пользователей:{}", users.size());
        if (users.isEmpty()) {
            return Set.of();
        }
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос POST /users.");

        if ((user.getEmail() == null) || (user.getLogin() == null) || (user.getBirthday() == null)) {
            throw new NullPointerException("Одно или несколько полей пользователя не заполнены.");
        }

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        } else if (!user.getBirthday().isBefore(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(id);
        log.debug("Добавлен пользователь:{}", user);
        users.add(user);
        id++;
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.debug("Получен запрос PUT /users.");

        if ((user.getEmail() == null) || (user.getLogin() == null) || (user.getBirthday() == null)) {
            throw new NullPointerException("Одно или несколько полей пользователя не заполнены.");
        }

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        } else if (!user.getBirthday().isBefore(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }


        for (User i : users) {
            if (i.getId() == user.getId()) {
                i.setId(user.getId());
                i.setName(user.getName());
                i.setLogin(user.getLogin());
                i.setEmail(user.getEmail());
                i.setBirthday(user.getBirthday());
            } else {
                throw new ValidationException("Данного пользователя не существует.");
            }
        }

        log.debug("Пользователь обновлен:{}", user);
        return user;
    }
}
