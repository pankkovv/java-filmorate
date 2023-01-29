package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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

    @BeforeAll
    public static void startServer() {
        users = new UserController();
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
        assertEquals("[]", users.findUser().toString());
        assertEquals(userOne, users.createUser(userOne));
        assertEquals(userTwo, users.updateUser(userTwo));
        assertEquals(userEmptyName.getLogin(), users.createUser(userEmptyName).getName());
        assertEquals(2, users.findUser().size());

    }

    //С неверными данными пользователя.
    @Test
    public void validationErrorTest() throws ValidationException {

        final ValidationException exceptionPostOne = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.createUser(userErrOne);
            }
        });

        final ValidationException exceptionPostTwo = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.createUser(userErrTwo);
            }
        });

        final ValidationException exceptionPostThree = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.createUser(userErrThree);
            }
        });

        final ValidationException exceptionPutOne = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.updateUser(userErrOne);
            }
        });

        final ValidationException exceptionPutTwo = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.updateUser(userErrTwo);
            }
        });

        final ValidationException exceptionPutThree = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                users.updateUser(userErrThree);
            }
        });

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exceptionPostOne.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы.", exceptionPostTwo.getMessage());
        assertEquals("Дата рождения не может быть в будущем.", exceptionPostThree.getMessage());

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exceptionPutOne.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы.", exceptionPutTwo.getMessage());
        assertEquals("Дата рождения не может быть в будущем.", exceptionPutThree.getMessage());
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
        assertEquals("Одно или несколько полей пользователя не заполнены.", exceptionPostOne.getMessage());
        assertEquals("Одно или несколько полей пользователя не заполнены.", exceptionPostTwo.getMessage());
    }
}