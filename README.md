# java-filmorate
Описание проекта
Бекэнд сервиса соцсети с возможностью оценивания фильмов.
Приложение позволяет взаимодествовать не только с фильмами, но и с другими пользователями, например, добавлять друг друга в друзья.

Приложение имеет следующие функции:
1. Создание фильмов и пользователей;
2. Хранение данных в БД;
3. Возможность ставить/убирать оценки фильмам;
4. Возможность добавления/удаления друзей;
5. Возможность просмотра список общих друзей с другими пользователеми;
6. Отображение списка фильмов, ранжированных по рейтингу.

### database-filmorate
![SCHEME](https://github.com/pankkovv/java-filmorate/blob/e74ff70271532223fd86e31b323e9acc4129c3a0/FilmorateER.jpg)

### Примеры запросов
User:
1. Создание нового пользователя: POST http://localhost:8080/users, в Request Body json с данными пользователя.
2. Обновление пользователя: PUT http://localhost:8080/users, в Request Body json с данными пользователя.
3. Получение списка пользователей: GET http://localhost:8080/users.
4. Получение пользователя по id: GET http://localhost:8080/users/{id}.

Film:
1. Создание нового фильма: POST http://localhost:8080/films, в Request Body json с данными фильма.
2. Обновление данных фильма: PUT http://localhost:8080/films, в Request Body json с данными фильма.
3. Получение списка фильмов: GET http://localhost:8080/films.
4. Получение фильма по id: GET http://localhost:8080/films/{id}.

Friends:
1. Получение списка друзей пользователя: GET http://localhost:8080/users/{id}/friends.
2. Получение списка общих друзей с другим пользователем: GET http://localhost:8080/users/{id}/friends/common/{friendId}.
3. Добавление в друзья: PUT http://localhost:8080/users/{id}/friends/{friendId}.
4. Удаление пользователя из друзей: DELETE http://localhost:8080/users/{id}/friends/{friendId}.

Like:
1. Поставить лайк фильму: PUT http://localhost:8080/films/{filmId}/like/{userId}.
2. Убрать лайк у фильма: DELETE http://localhost:8080/films/{filmId}/like/{userId}.
3. Получить список популярных фильмов: GET http://localhost:8080/films/popular.

### Примеры запросов к БД
1. Получение всех фильмов: "select * from films"
2. Получение всех пользователей: "select * from users"
3. Получение всех жанров: "select * from gerne"
4. Получение всех вариантов рейтинга: "select * from mpa"

## Стек
- Java SE 9
- Spring Boot
- Spring JDBC
- Database H2
- JUnit

## Шаги по запуску приложения
- Склонировать репозиторий
- Проверить наличие всех зависиостей синхронизировав pom.xml с локальным репозиторием
- Запустить билд через консоль или среду разработки
- Взаимодествовать с приложением через API http://localhost:8080/
- Взаимодействовать с БД через API http://localhost:8080/h2-console с username= sa и password= password

----
Приложение написано на Java и протестировано. Пример кода:
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
----
Приложение создано в рамках прохождения курса Java-разработчик от [Яндекс-Практикум](https://practicum.yandex.ru/java-developer/ "Тут учат Java!") 
