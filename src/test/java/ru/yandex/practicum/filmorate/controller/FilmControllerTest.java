package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    static FilmController films;
    Film filmOne;
    Film filmTwo;
    Film filmEmpty;
    Film filmErrOne;
    Film filmErrTwo;
    Film filmErrThree;
    Film filmErrFour;

    @BeforeAll
    public static void startServer() {
        films = new FilmController();
    }

    @BeforeEach
    public void assistant() {
        filmOne = Film.builder().id(1).name("test").description("test").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmTwo = Film.builder().id(1).name("noTest").description("noTest").releaseDate(LocalDate.of(2000, 1, 1)).duration(200).build();
        filmEmpty = Film.builder().build();
        filmErrOne = Film.builder().id(1).name("").description("testErr").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmErrTwo = Film.builder().id(1).name("test").description("testdsflkfdgjkjdfhgjhfdjgjkdfgkjfdghjkdfhgjkdfjkghkdfhgkdfhghdfjkgkjdfhgkjhdfkjghkdfjhgkjdfhgjhdfkjghkjdfhgkjdfhgkhdfkjghkjdfhgkjdfhgkjdfhgjkdfkjghdfjkhgkjdfhgkjdfhkghdfkghkdfjhgkjhfgkjhdfkjghkjdfhgkjdfhgjhdfkgkdfjhgkjdfhkghdfkjghkjfdhgjkdfkjkgfkfgkjghkjdfkjfhkgjhgjkhfkj").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmErrThree = Film.builder().id(1).name("test").description("test").releaseDate(LocalDate.of(1800, 1, 1)).duration(100).build();
        filmErrFour = Film.builder().id(1).name("test").description("test").releaseDate(LocalDate.of(2000, 1, 1)).duration(-100).build();
    }

    //Со стандартным поведением.
    @Test
    public void validationTest() throws ValidationException {
        assertEquals("[]", films.findFilm().toString());
        assertEquals(filmOne, films.createFilm(filmOne));
        assertEquals(filmTwo, films.updateFilm(filmTwo));
        assertEquals(1, films.findFilm().size());
    }

    //С неверными данными пользователя.
    @Test
    public void validationErrorTest() throws ValidationException {

        final ValidationException exceptionPostOne = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.createFilm(filmErrOne);
            }
        });

        final ValidationException exceptionPostTwo = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.createFilm(filmErrTwo);
            }
        });

        final ValidationException exceptionPostThree = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.createFilm(filmErrThree);
            }
        });

        final ValidationException exceptionPostFour = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.createFilm(filmErrFour);
            }
        });

        final ValidationException exceptionPutOne = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrOne);
            }
        });

        final ValidationException exceptionPutTwo = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrTwo);
            }
        });

        final ValidationException exceptionPutThree = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrThree);
            }
        });

        final ValidationException exceptionPutFour = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrFour);
            }
        });

        assertEquals("Название не может быть пустым.", exceptionPostOne.getMessage());
        assertEquals("Максимальная длина описания — 200 символов.", exceptionPostTwo.getMessage());
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exceptionPostThree.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", exceptionPostFour.getMessage());

        assertEquals("Название не может быть пустым.", exceptionPutOne.getMessage());
        assertEquals("Максимальная длина описания — 200 символов.", exceptionPutTwo.getMessage());
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exceptionPutThree.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", exceptionPutFour.getMessage());
    }

    //Без данных пользователя
    @Test
    public void validationEmptyTest() {
        final NullPointerException exceptionPostOne = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.createFilm(filmEmpty);
            }
        });

        final NullPointerException exceptionPostTwo = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmEmpty);
            }
        });
        assertEquals("Одно или несколько полей фильма не заполнены.", exceptionPostOne.getMessage());
        assertEquals("Одно или несколько полей фильма не заполнены.", exceptionPostTwo.getMessage());
    }
}