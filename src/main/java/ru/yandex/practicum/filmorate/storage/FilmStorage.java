package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {
    Set<Film> findFilm();
    Film createFilm(Film film) throws ValidationException;
    Film updateFilm(Film film) throws ValidationException;

}
