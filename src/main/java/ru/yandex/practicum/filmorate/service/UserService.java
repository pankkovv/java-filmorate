package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findFriendsUserId(Integer id) {
        User user = userStorage.findUser().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", id)));

        return userStorage.findUser().stream()
                .filter(s -> user.getFriends().contains(s.getId()))
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) {
        User userOne = userStorage.findUser().stream()
                .filter(p -> p.getId() == id)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", id)));

        User userTwo = userStorage.findUser().stream()
                .filter(p -> p.getId() == otherId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", otherId)));

        Set<Integer> setCommonFriend = userOne.getFriends().stream().filter(s -> userTwo.getFriends().contains(s)).collect(Collectors.toSet());

        return userStorage.findUser().stream()
                .filter(s -> setCommonFriend.contains(s.getId()))
                .collect(Collectors.toList());
    }

    public Set<Integer> addFriend(Integer userOneId, Integer userTwoId) {
        userStorage.findUser().stream()
                .filter(p -> p.getId() == userTwoId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userTwoId)))
                .addFriends(userOneId);

        userStorage.findUser().stream()
                .filter(p -> p.getId() == userOneId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userOneId)))
                .addFriends(userTwoId);

        return userStorage.findUser().stream()
                .filter(p -> p.getId() == userOneId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userOneId)))
                .getFriends();
    }

    public Set<Integer> deleteFriend(Integer userOneId, Integer userTwoId) {
        userStorage.findUser().stream()
                .filter(p -> p.getId() == userTwoId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userTwoId)))
                .removeFriends(userOneId);

        userStorage.findUser().stream()
                .filter(p -> p.getId() == userOneId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userOneId)))
                .removeFriends(userTwoId);

        return userStorage.findUser().stream()
                .filter(p -> p.getId() == userOneId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userOneId)))
                .getFriends();
    }
}
