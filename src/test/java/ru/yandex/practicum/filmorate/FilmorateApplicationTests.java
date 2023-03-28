package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.storage.FriendsDbStorage;
import ru.yandex.practicum.filmorate.dao.storage.RateDbStorage;
import ru.yandex.practicum.filmorate.dao.storage.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final GenresDbStorage genresStorage;
    private final MpaDbStorage mpaStorage;
    private final RateDbStorage rateService;
    private final FriendsDbStorage friendsService;
    private static User user = User.builder().id(1).email("test@email.ru").login("test").name("test").birthday(LocalDate.of(2000, 1, 1)).build();
    private static User userTwo = User.builder().id(1).email("test@email.ru").login("test").name("test").birthday(LocalDate.of(2000, 1, 1)).build();
    private static User userThree = User.builder().id(1).email("test@email.ru").login("test").name("test").birthday(LocalDate.of(2000, 1, 1)).build();
    private static Film film = Film.builder().id(1).name("test").description("test").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).rate(0).mpa(Mpa.builder().id(1).name("G").build()).build();

    @Test
    public void testDb() throws Throwable {
        //create users
        Optional<User> userCreate = userStorage.createUser(user);
        Optional<User> userTwoCreate = userStorage.createUser(userTwo);
        Optional<User> userThreeCreate = userStorage.createUser(userThree);

        assertThat(userCreate)
                .isEqualTo(Optional.of(user));

        assertThat(userTwoCreate)
                .isEqualTo(Optional.of(userTwo));

        assertThat(userThreeCreate)
                .isEqualTo(Optional.of(userThree));

        //update user
        Optional<User> userUpdate = userStorage.updateUser(user);

        assertThat(userUpdate)
                .isEqualTo(Optional.of(user));

        //find user
        List<User> userFind = userStorage.findUser();
        Optional<User> userFindId = userStorage.findUserId(user.getId());

        assertThat(userFindId)
                .isEqualTo(Optional.of(user));

        assertThat(userFind)
                .isEqualTo(List.of(user, userTwo, userThree));

        //add friend
        List<User> addFriend = friendsService.addFriend(user.getId(), userTwo.getId());
        List<User> addFriendCommonOne = friendsService.addFriend(user.getId(), userThree.getId());
        List<User> addFriendCommonTwo = friendsService.addFriend(userTwo.getId(), userThree.getId());

        assertThat(addFriend)
                .isEqualTo(List.of(userTwo));

        assertThat(addFriendCommonOne)
                .isEqualTo(List.of(userTwo, userThree));

        assertThat(addFriendCommonTwo)
                .isEqualTo(List.of(userThree));

        //find friends user
        List<User> findFriendsUserId = friendsService.findFriendsUserId(userTwo.getId());
        List<User> findCommonFriends = friendsService.findCommonFriends(user.getId(), userTwo.getId());

        assertThat(findFriendsUserId)
                .isEqualTo(List.of(userThree));

        assertThat(findCommonFriends)
                .isEqualTo(List.of(userThree));

        //delete friend
        List<User> deleteFriend = friendsService.deleteFriend(userTwo.getId(), userThree.getId());
        List<User> findFriendsUserIdOne = friendsService.findFriendsUserId(userTwo.getId());

        assertThat(deleteFriend)
                .isEqualTo(List.of());

        assertThat(findFriendsUserIdOne)
                .isEqualTo(List.of());

        //create film
        Optional<Film> filmCreate = filmStorage.createFilm(film);

        assertThat(filmCreate)
                .isEqualTo(Optional.of(film));

        //update film
        Optional<Film> filmUpdate = filmStorage.updateFilm(film);

        assertThat(filmUpdate)
                .isEqualTo(Optional.of(film));

        //find film
        List<Film> filmFind = filmStorage.findFilm();
        List<Film> filmFindPopular = filmStorage.findPopularFilm(1);
        Optional<Film> filmFindId = filmStorage.findFilmId(film.getId());

        assertThat(filmFind)
                .isEqualTo(List.of(film));

        assertThat(filmFindPopular)
                .isEqualTo(List.of(film));

        assertThat(filmFindId)
                .isEqualTo(Optional.of(film));

        //add rate to film
        Optional<Film> addLike = rateService.addLike(film.getId(), userCreate.get().getId());
        List<Long> findUserRateFilmAdd = rateService.findUserRateFilm(film.getId());

        assertThat(addLike)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 1)
                );

        assertThat(findUserRateFilmAdd)
                .isEqualTo(List.of(user.getId()));

        //delete rate from film
        Optional<Film> removeLike = rateService.removeLike(film.getId(), user.getId());
        List<Long> findUserRateFilmRemove = rateService.findUserRateFilm(film.getId());

        assertThat(removeLike)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0)
                );

        assertThat(findUserRateFilmRemove)
                .isEqualTo(List.of());

        //add genres to film
        genresStorage.addGenresFilm(film.getId(), List.of(genreStorage.findGenreId(1).get()));
        List<Genre> genresFilmAdd = genresStorage.findGenresFilmId(film.getId());

        assertThat(genresFilmAdd)
                .isEqualTo(List.of(genreStorage.findGenreId(1).get()));

        //delete genres from film
        genresStorage.removeGenresFilm(film.getId());
        List<Genre> genresFilmRemove = genresStorage.findGenresFilmId(film.getId());

        assertThat(genresFilmRemove)
                .isEqualTo(List.of());

    }

    @Test
    public void testGenreDb() {
        List<Genre> genreFind = genreStorage.findGenre();
        Optional<Genre> genreFindId = genreStorage.findGenreId(1);

        assertThat(genreFind)
                .isEqualTo(List.of(
                        Genre.builder().id(1).name("Комедия").build(),
                        Genre.builder().id(2).name("Драма").build(),
                        Genre.builder().id(3).name("Мультфильм").build(),
                        Genre.builder().id(4).name("Триллер").build(),
                        Genre.builder().id(5).name("Документальный").build(),
                        Genre.builder().id(6).name("Боевик").build()
                ));

        assertThat(genreFindId)
                .isEqualTo(Optional.of(Genre.builder().id(1).name("Комедия").build()));

    }

    @Test
    public void testMpaDb() {
        List<Mpa> mpaFind = mpaStorage.findMpa();
        Optional<Mpa> mpaFindId = mpaStorage.findMpaId(1);

        assertThat(mpaFind)
                .isEqualTo(List.of(
                        Mpa.builder().id(1).name("G").build(),
                        Mpa.builder().id(2).name("PG").build(),
                        Mpa.builder().id(3).name("PG-13").build(),
                        Mpa.builder().id(4).name("R").build(),
                        Mpa.builder().id(5).name("NC-17").build()
                ));

        assertThat(mpaFindId)
                .isEqualTo(Optional.of(Mpa.builder().id(1).name("G").build()));
    }

}
