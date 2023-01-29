package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Set<Film> films = new HashSet<Film>();
    private int id = 1;

    @GetMapping
    public Set<Film> findFilm() {
        log.debug("Количество фильмов:{}", films.size());
        if (films.isEmpty()) {
            return Set.of();
        }
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос POST /films.");

        if ((film.getName() == null) || (film.getDescription() == null) || (film.getReleaseDate() == null) || (film.getDuration() == null)) {
            throw new NullPointerException("Одно или несколько полей фильма не заполнены.");
        }

        if (film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }

        film.setId(id);
        log.debug("Добавлен фильм:{}", film);
        films.add(film);
        id++;
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос PUT /films.");

        if ((film.getName() == null) || (film.getDescription() == null) || (film.getReleaseDate() == null) || (film.getDuration() == null)) {
            throw new NullPointerException("Одно или несколько полей фильма не заполнены.");
        }

        if (film.getName().isEmpty()) {
            throw new ValidationException("Название не может быть пустым.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }

        for (Film i : films) {
            if (i.getId() == film.getId()) {
                i.setId(film.getId());
                i.setName(film.getName());
                i.setDescription(film.getDescription());
                i.setReleaseDate(film.getReleaseDate());
                i.setDuration(film.getDuration());
            } else {
                throw new ValidationException("Данного фильма не существует.");
            }
        }

        log.debug("Фильм обновлен:{}", film);
        return film;
    }
}
