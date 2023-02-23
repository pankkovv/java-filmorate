package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Set<Film> films = new HashSet<Film>();
    private int id = 1;

    @Override
    public Set<Film> findFilm() {
        log.debug("Количество фильмов:{}", films.size());
        if (films.isEmpty()) {
            return Set.of();
        }
        return films;
    }

    @Override
    public Film findFilmId(Integer id) {
        return films.stream().
                filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм № %d не найден", id)));
    }

    @Override
    public Film createFilm(Film film) throws ValidationException {
        log.debug("Получен запрос POST /films.");

        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }

        film.setId(id);
        log.debug("Добавлен фильм:{}", film);
        films.add(film);
        id++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        log.debug("Получен запрос PUT /films.");

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
