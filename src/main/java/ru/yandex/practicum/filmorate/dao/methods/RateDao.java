package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface RateDao {
    List<Long> findUserRateFilm(long filmId);

    Optional<Film> addLike(long filmId, long userId);

    Optional<Film> removeLike(long filmId, long userId);
}
