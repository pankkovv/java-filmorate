package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDao {
    List<Genre> findGenresFilmId(Integer id);

    void addGenresFilm(Integer filmId, Integer genresId);

    void removeGenresFilm(Integer filmId);
}
