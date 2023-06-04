# java-filmorate
---
### Приложение соцсеть для оценивания фильмов 

Мое приложение умеет следующие функции:
1. Хранить фильмы и пользователей в БД;
2. Пользователь может ставить/убирать оценки фильмам;
3. Пользователи могут добавлять/убирать другого пользоватлея в друзья;
4. Пользователи могут просматривать список общих друзей с другим пользователем;
5. Ранжировать фильмы по рейтингу.

#### database-filmorate
![FilmorateER alt][(https://github.com/pankkovv/java-filmorate/blob/add-databse/FilmorateER.jpg?raw=true)](https://github.com/pankkovv/java-filmorate/blob/main/FilmorateER.jpg)

#### Примеры запросов
User:
1. Создание нового пользователя: POST http://localhost:8080/users, в Request Body json с данными пользователя.
2. Обновление пользователя: PUT http://localhost:8080/users, в Request Body json с данными пользователя.
3. Получние списка пользователей: GET http://localhost:8080/users.
4. Получние пользователя по id: GET http://localhost:8080/users/{id}.

Film:
1. Создание нового фильма: POST http://localhost:8080/films, в Request Body json с данными фильма.
2. Обновление фильма: PUT http://localhost:8080/films, в Request Body json с данными фильма.
3. Получние списка фильмов: GET http://localhost:8080/films.
4. Получние фильма по id: GET http://localhost:8080/films/{id}.

Friends:
1. Получние списка друзей пользователя: GET http://localhost:8080/users/{id}/friends.
2. Получние списка общих друзей с другим пользователем: GET http://localhost:8080/users/{id}/friends/common/{friendId}.
3. Добавление в друзья: PUT http://localhost:8080/users/{id}/friends/{friendId}.
4. Удаление пользователя из списка друзей: DELETE http://localhost:8080/users/{id}/friends/{friendId}.

Like:
1. Поставить лайк фильму: PUT http://localhost:8080/films/{filmId}/like/{userId}.
2. Убрать лайк у фильма: DELETE http://localhost:8080/films/{filmId}/like/{userId}.
3. Получить списко популярных фильмов: GET http://localhost:8080/films/popular.

# Пример запроса к БД
1. Получение всех фильмов: "select * from films"
2. Получение всех Пользователей: "select * from users"
3. Получение всех жанров: "select * from gerne"
4. Получение всех вариантов рейтинг: "select * from mpa"

Взаимодействие с БД реализовано за счет Spring JDBC. 
REST API создано на базе Spring Boot.

Приложение написано на Java. Пример кода:
```java
@Override
    public Optional<Film> findFilmId(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        if (filmRows.next()) {
            Film film = Film.builder().id(filmRows.getInt("id")).name(filmRows.getString("name")).description(filmRows.getString("description")).releaseDate(filmRows.getDate("release_date").toLocalDate()).duration(filmRows.getInt("duration")).rate(filmRows.getInt("rate")).mpa(mpaDao.findMpaId(filmRows.getInt("mpa_id")).get()).genres(genresDao.findGenresFilmId(filmRows.getInt("id"))).build();

            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);

            throw new NotFoundException("Фильм с идентификатором " + id + " не найден.");
        }
    }
```
------

Приложение создано в рамках прохождения курса Java-разработчик от [Яндекс-Практикум](https://practicum.yandex.ru/java-developer/ "Тут учат Java!") 
