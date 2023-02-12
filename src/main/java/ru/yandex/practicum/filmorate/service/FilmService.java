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

    public Integer findAllLikes(Integer filmId) {
        return filmStorage.findFilm().stream()
                .filter(p -> p.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм № %d не найден", filmId)))
                .getLikes()
                .size();
    }

    public List<Film> findPopularFilm(Integer count) {
        return filmStorage.findFilm().stream()
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
        userStorage.findUser().stream()
                .filter(p -> p.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userId)));

        filmStorage.findFilm().stream()
                .filter(p -> p.getId() == filmId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Фильм № %d не найден", filmId)))
                .addLikes(userId);

        return filmStorage.findFilm().stream()
                .filter(p -> p.getId() == filmId)
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Фильм № %d не найден", filmId)))
                .getLikes()
                .size();
    }

    public Integer removeLike(Integer filmId, Integer userId) {
        userStorage.findUser().stream()
                .filter(p -> p.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь № %d не найден", userId)));

        filmStorage.findFilm().stream()
                .filter(p -> p.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм № %d не найден", filmId)))
                .removeLikes(userId);

        return filmStorage.findFilm().stream()
                .filter(p -> p.getId() == filmId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм № %d не найден", filmId)))
                .getLikes()
                .size();
    }
}
