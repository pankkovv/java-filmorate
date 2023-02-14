package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public UserStorage getUserStorage(){return userStorage;}
    public List<User> findFriendsUserId(Integer id) {
        return userStorage.findUser()
                .stream()
                .filter(s -> userStorage
                        .findUserId(id)
                        .getFriends()
                        .contains(s.getId()))
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Integer id, Integer otherId) {
        Set<Integer> setCommonFriend = userStorage.findUserId(id)
                .getFriends()
                .stream()
                .filter(s -> userStorage
                        .findUserId(otherId)
                        .getFriends()
                        .contains(s))
                .collect(Collectors.toSet());

        return userStorage.findUser().stream()
                .filter(s -> setCommonFriend.contains(s.getId()))
                .collect(Collectors.toList());
    }

    public Set<Integer> addFriend(Integer userOneId, Integer userTwoId) {
        userStorage.findUserId(userTwoId)
                .addFriends(userOneId);
        userStorage.findUserId(userOneId)
                .addFriends(userTwoId);

        return userStorage.findUserId(userOneId)
                .getFriends();
    }

    public Set<Integer> deleteFriend(Integer userOneId, Integer userTwoId) {
        userStorage.findUserId(userTwoId)
                .removeFriends(userOneId);
        userStorage.findUserId(userOneId)
                .removeFriends(userTwoId);

        return userStorage.findUserId(userOneId)
                .getFriends();
    }
}
