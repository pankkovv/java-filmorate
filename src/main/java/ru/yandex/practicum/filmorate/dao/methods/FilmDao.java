package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    List<Film> findFilm();

    Optional<Film> findFilmId(long id) throws NotFoundException;

    List<Film> findPopularFilm(Integer count);

    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film) throws ValidationException;

}
