package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    List<Genre> findGenre();

    Optional<Genre> findGenreId(long id);
}
