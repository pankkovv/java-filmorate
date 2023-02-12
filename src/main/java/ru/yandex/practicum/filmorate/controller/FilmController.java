package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private InMemoryFilmStorage inMemoryFilmStorage;
    private FilmService filmService;

    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Set<Film> findFilm() {
        return inMemoryFilmStorage.findFilm();
    }

    @GetMapping("/{id}")
    public Film findFilmId(@PathVariable int id) {
        return inMemoryFilmStorage.findFilmId(id);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilm(@RequestParam(value = "count", required = false, defaultValue = "10") int count) {
        return filmService.findPopularFilm(count);

    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addLike(id, userId);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.removeLike(id, userId);
    }
}
