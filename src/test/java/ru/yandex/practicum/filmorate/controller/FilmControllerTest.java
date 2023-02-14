package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

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
    FilmService filmService;
    InMemoryFilmStorage filmsNormal = new InMemoryFilmStorage();

    @BeforeEach
    public void start() {
        films = new FilmController(filmService);
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
        assertEquals("[]", filmsNormal.findFilm().toString());
        assertEquals(filmOne, filmsNormal.createFilm(filmOne));
        assertEquals(filmTwo, filmsNormal.updateFilm(filmTwo));
        assertEquals(1, filmsNormal.findFilm().size());
    }

    //С неверными данными пользователя.
    @Test
    public void validationErrorTest() throws ValidationException {

        final NullPointerException exceptionPostOne = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws NullPointerException, ValidationException {
                films.createFilm(filmErrOne);
            }
        });

        final NullPointerException exceptionPostTwo = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws NullPointerException, ValidationException {
                films.createFilm(filmErrTwo);
            }
        });

        final NullPointerException exceptionPostThree = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws NullPointerException, ValidationException {
                films.createFilm(filmErrThree);
            }
        });

        final NullPointerException exceptionPostFour = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws NullPointerException, ValidationException {
                films.createFilm(filmErrFour);
            }
        });

        final NullPointerException exceptionPutOne = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrOne);
            }
        });

        final NullPointerException exceptionPutTwo = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrTwo);
            }
        });

        final NullPointerException exceptionPutThree = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrThree);
            }
        });

        final NullPointerException exceptionPutFour = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws ValidationException {
                films.updateFilm(filmErrFour);
            }
        });

        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPostOne.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPostTwo.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPostThree.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPostFour.getMessage());

        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPutOne.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPutTwo.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPutThree.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPutFour.getMessage());
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
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPostOne.getMessage());
        assertEquals("Cannot invoke \"ru.yandex.practicum.filmorate.service.FilmService.getFilmStorage()\" because \"this.filmService\" is null", exceptionPostTwo.getMessage());
    }
}