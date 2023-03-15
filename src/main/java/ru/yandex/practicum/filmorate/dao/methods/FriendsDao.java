package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {
    List<User> findFriendsUserId(Integer id);

    List<User> findCommonFriends(Integer id, Integer otherId);

    List<User> addFriend(Integer userOneId, Integer userTwoId) throws Throwable;

    List<User> deleteFriend(Integer userOneId, Integer userTwoId) throws Throwable;
}
