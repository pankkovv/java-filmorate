package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDao {
    List<Genre> findGenresFilmId(long id);

    void addGenresFilm(long filmId, long genresId);

    void removeGenresFilm(long filmId);
}
