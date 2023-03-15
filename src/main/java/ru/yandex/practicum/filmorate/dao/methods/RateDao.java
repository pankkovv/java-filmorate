package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface RateDao {
    List<Integer> findUserRateFilm(Integer filmId);

    Optional<Film> addLike(Integer filmId, Integer userId) throws Throwable;

    Optional<Film> removeLike(Integer filmId, Integer userId) throws Throwable;
}
