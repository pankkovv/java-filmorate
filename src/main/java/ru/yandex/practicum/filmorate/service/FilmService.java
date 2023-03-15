package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.methods.FilmDao;
import ru.yandex.practicum.filmorate.dao.methods.GenreDao;
import ru.yandex.practicum.filmorate.dao.methods.MpaDao;
import ru.yandex.practicum.filmorate.dao.methods.RateDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private FilmDao filmDao;
    private RateDao rateDao;
    private MpaDao mpaDao;
    private GenreDao genreDao;

    @Autowired
    public FilmService(FilmDao filmDao, RateDao rateDao, MpaDao mpaDao, GenreDao genreDao) {
        this.filmDao = filmDao;
        this.rateDao = rateDao;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
    }

    public List<Film> findAllFilm(){
        return filmDao.findFilm();
    }

    public Optional<Film> findFilmId(Integer id) {
        return filmDao.findFilmId(id);
    }

    public List<Film> findPopularFilm(Integer count){
        return filmDao.findPopularFilm(count);
    }

    public Optional<Film> createFilm(Film film) throws Throwable {
        return filmDao.createFilm(film);
    }

    public Optional<Film> updateFilm(Film film) throws ValidationException {
        return filmDao.updateFilm(film);
    }

    public Optional<Film> addLike(Integer filmId, Integer userId) throws Throwable {
        return rateDao.addLike(filmId, userId);
    }

    public Optional<Film> removeLike(Integer filmId, Integer userId) throws Throwable {
        return rateDao.removeLike(filmId, userId);
    }

    public List<Mpa> findMpa(){
        return mpaDao.findMpa();
    }

    public Optional<Mpa> findMpaId(Integer id){
        return mpaDao.findMpaId(id);
    }

    public List<Genre> findGenre(){
        return genreDao.findGenre();
    }

    public Optional<Genre> findGenreId(Integer id){
        return genreDao.findGenreId(id);
    }
}
