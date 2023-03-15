package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findFilm() {
        return filmService.findAllFilm();
    }

    @GetMapping("/{id}")
    public Optional<Film> findFilmId(@PathVariable int id) throws SQLException {
        return filmService.findFilmId(id);
    }

    @PostMapping
    public Optional<Film> createFilm(@Valid @RequestBody Film film) throws Throwable {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilm(@RequestParam(value = "count", required = false, defaultValue = "10") int count) {
        return filmService.findPopularFilm(count);

    }

    @PutMapping("/{filmId}/like/{userId}")
    public Optional<Film> addLike(@PathVariable int filmId, @PathVariable int userId) throws Throwable {
        return filmService.addLike(filmId, userId);
    }


    @DeleteMapping("/{filmId}/like/{userId}")
    public Optional<Film> removeLike(@PathVariable int filmId, @PathVariable int userId) throws Throwable {
        return filmService.removeLike(filmId, userId);
    }
}
