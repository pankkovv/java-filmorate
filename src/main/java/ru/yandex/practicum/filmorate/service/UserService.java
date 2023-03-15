package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.methods.FriendsDao;
import ru.yandex.practicum.filmorate.dao.methods.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
public class UserService {
    private UserDao userDao;
    private FriendsDao friendsDao;

    @Autowired
    public UserService(UserDao userDao, FriendsDao friendsDao) {
        this.userDao = userDao;
        this.friendsDao = friendsDao;
    }

    public List<User> findUser(){
        return userDao.findUser();
    }

    public Optional<User> findUserId(Integer id) {
        return userDao.findUserId(id);
    }

    public Optional<User> createUser(User user) throws ValidationException {
        return userDao.createUser(user);
    }

    public Optional<User> updateUser(User user) throws ValidationException {
        return userDao.updateUser(user);
    }

    public List<User> findFriendsUserId(Integer id){
        return friendsDao.findFriendsUserId(id);
    }

    public List<User> findCommonFriends(Integer id, Integer otherId){
        return friendsDao.findCommonFriends(id, otherId);
    }

    public List<User> addFriend(Integer userOneId, Integer userTwoId) throws Throwable {
        return friendsDao.addFriend(userOneId, userTwoId);
    }

    public List<User> deleteFriend(Integer userOneId, Integer userTwoId) throws Throwable {
        return friendsDao.deleteFriend(userOneId, userTwoId);
    }
}
