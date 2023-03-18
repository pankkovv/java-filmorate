package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> findUser() {
        return userService.findUser();
    }

    @GetMapping("/{id}")
    public Optional<User> findUserId(@PathVariable int id) {
        return userService.findUserId(id);
    }

    @PostMapping
    public Optional<User> createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public Optional<User> updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsUserId(@PathVariable int id) {
        return userService.findFriendsUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> newFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.deleteFriend(id, friendId);
    }
}
