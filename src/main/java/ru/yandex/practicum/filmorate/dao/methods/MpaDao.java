package ru.yandex.practicum.filmorate.dao.methods;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {
    List<Mpa> findMpa();

    Optional<Mpa> findMpaId(Integer id);
}
