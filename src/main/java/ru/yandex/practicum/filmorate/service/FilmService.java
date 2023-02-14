package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public FilmStorage getFilmStorage(){return filmStorage;}
    public Integer findAllLikes(Integer filmId) {
        return filmStorage.findFilmId(filmId)
                .getLikes()
                .size();
    }

    public List<Film> findPopularFilm(Integer count) {
        return filmStorage.findFilm()
                .stream()
                .sorted((p0, p1) -> {
                    if (p0.getLikes().size() > p1.getLikes().size()) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    public Integer addLike(Integer filmId, Integer userId) {
        userStorage.findUserId(userId);
        filmStorage.findFilmId(filmId)
                .addLikes(userId);

        return filmStorage.findFilmId(filmId)
                .getLikes()
                .size();
    }

    public Integer removeLike(Integer filmId, Integer userId) {
        userStorage.findUserId(userId);
        filmStorage.findFilmId(filmId)
                .removeLikes(userId);

        return filmStorage.findFilmId(filmId)
                .getLikes()
                .size();
    }
}
