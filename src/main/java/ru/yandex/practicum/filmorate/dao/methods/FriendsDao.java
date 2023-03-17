package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.User;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface FriendsDao {
    List<User> findFriendsUserId(long id);

    List<User> findCommonFriends(long id, long otherId);

    List<User> addFriend(long userOneId, long userTwoId) throws ValidationException, ru.yandex.practicum.filmorate.exception.ValidationException;

    List<User> deleteFriend(long userOneId, long userTwoId) throws ValidationException, ru.yandex.practicum.filmorate.exception.ValidationException;
}
