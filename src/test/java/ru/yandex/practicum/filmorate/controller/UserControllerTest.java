package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static UserController users;
    User userOne;
    User userTwo;
    User userEmptyName;
    User userEmpty;
    User userErrOne;
    User userErrTwo;
    User userErrThree;
    UserService userService;
    InMemoryUserStorage usersNormal = new InMemoryUserStorage();

    @BeforeEach
    public void start() {
        users = new UserController(userService);
    }

    @BeforeEach
    public void assistant() {
        userOne = User.builder().id(1).email("test@ya.ru").login("testing").name("test").birthday(LocalDate.of(2000, 1, 1)).build();
        userTwo = User.builder().id(1).email("noTest@ya.ru").login("noTesting").name("noTest").birthday(LocalDate.of(2000, 1, 1)).build();
        userEmptyName = User.builder().id(1).email("noTest@ya.ru").login("noTesting").birthday(LocalDate.of(2000, 1, 1)).build();
        userEmpty = User.builder().build();
        userErrOne = User.builder().id(3).email("errTestya.ru").login("err").name("err").birthday(LocalDate.of(2000, 1, 1)).build();
        userErrTwo = User.builder().id(4).email("errTest@ya.ru").login(" ").name("err").birthday(LocalDate.of(2000, 1, 1)).build();
        userErrThree = User.builder().id(5).email("errTest@ya.ru").login("err").name("err").birthday(LocalDate.of(2024, 1, 1)).build();
    }

    //Со стандартным поведением.
    @Test
    public void validationTest() throws ValidationException {
        assertEquals("[]", usersNormal.findUser().toString());
        assertEquals(userOne, usersNormal.createUser(userOne));
        assertEquals(userTwo, usersNormal.updateUser(userTwo));
        assertEquals(userEmptyName.getLogin(), usersNormal.createUser(userEmptyName).getName());
        assertEquals(2, usersNormal.findUser().size());

    }

    //С неверными данными пользователя.
    @Test
    public void validationErrorTest() throws ValidationException {

        final NullPointerException exceptionPostOne = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.createUser(userErrOne);
            }
        });

        final NullPointerException exceptionPostTwo = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.createUser(userErrTwo);
            }
        });

        final NullPointerException exceptionPostThree = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.createUser(userErrThree);
            }
        });

        final NullPointerException exceptionPutOne = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.updateUser(userErrOne);
            }
        });

        final NullPointerException exceptionPutTwo = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.updateUser(userErrTwo);
            }
        });

        final NullPointerException exceptionPutThree = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.updateUser(userErrThree);
            }
        });

        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPostOne.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPostTwo.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPostThree.getMessage());

        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPutOne.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPutTwo.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPutThree.getMessage());
    }

    //Без данных пользователя
    @Test
    public void validationEmptyTest() {
        final NullPointerException exceptionPostOne = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.createUser(userEmpty);
            }
        });

        final NullPointerException exceptionPostTwo = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.updateUser(userEmpty);
            }
        });
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPostOne.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.UserService.getUserStorage()\" because \"this.userService\" is null", exceptionPostTwo.getMessage());
    }
}