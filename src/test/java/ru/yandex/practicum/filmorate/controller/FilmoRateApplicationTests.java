package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Additional.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.Additional.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenresDbStorage genresDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private User user1;
    private User user2;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        user1 = User.builder()
                .name("Name_1")
                .email("mail@mail.ru")
                .login("Login_1")
                .birthday(LocalDate.parse("1976-06-10"))
                .build();
        user2 = User.builder()
                .name("Name_2")
                .email("mail2@mail.ru")
                .login("Login_2")
                .birthday(LocalDate.parse("1984-03-07"))
                .build();
        film1 = Film.builder()
                .name("Film_1")
                .description("Description_1")
                .releaseDate(LocalDate.of(2020, 2, 12))
                .duration(120)
                .build();
        film1.setMpa(new Mpa(1, "G"));
        film1.setGenres(new HashSet<>(Arrays.asList
                (new Genre(1, "Комедия"),
                        new Genre(4, "Триллер"))));
        film2 = Film.builder()
                .name("Film_2")
                .description("Description_2")
                .releaseDate(LocalDate.of(2023, 3, 30))
                .duration(180)
                .build();
        film2.setMpa(new Mpa(2, "PG"));
    }

    @Test
    public void testCreateAndFindUserById() {
        user1 = userStorage.createUser(user1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.findUserById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllUsers() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        List<User> users = userStorage.findAllUsers();
        assertThat(users.contains(user1)).isTrue();
        assertThat(users.contains(user2)).isTrue();
    }

    @Test
    public void testRemoveUser() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        userStorage.removeUser(user2.getId());
        List<User> listUsers = userStorage.findAllUsers();
        assertThat(listUsers.contains(user1)).isTrue();
        assertThat(listUsers.contains(user2)).isFalse();
    }

    @Test
    public void updateUser() {
        user1 = userStorage.createUser(user1);
        Optional<User> userBeforeUpdate = Optional.ofNullable(userStorage.findUserById(1));
        assertThat(userBeforeUpdate)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Name_1")
                                .hasFieldOrPropertyWithValue("email", "mail@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "Login_1")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1976, 6, 10))
                                .hasFieldOrPropertyWithValue("id", 1)
                );
        User updatedUser = User.builder()
                .id(1)
                .name("New_name")
                .email("new@mail.ru")
                .login("New_login")
                .birthday(LocalDate.parse("2000-01-01"))
                .build();
        userStorage.updateUser(updatedUser);
        Optional<User> userAfterUpdate = Optional.ofNullable(userStorage.findUserById(1));
        assertThat(userAfterUpdate)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "New_name")
                                .hasFieldOrPropertyWithValue("email", "new@mail.ru")
                                .hasFieldOrPropertyWithValue("login", "New_login")
                                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 1))
                                .hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void addFriendOneWayFriendship() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        userStorage.addFriend(user1.getId(), user2.getId());
        List<Integer> friendsOfFirst = userStorage.getFriends(user1.getId());
        List<Integer> friendsOfSecond = userStorage.getFriends(user2.getId());
        assertThat(friendsOfFirst.contains(user2.getId())).isTrue();
        assertThat(friendsOfSecond.contains(user1.getId())).isFalse();
    }

    @Test
    public void addFriendTwoWaysFriendship() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user2.getId(), user1.getId());
        List<Integer> friendsOfFirst = userStorage.getFriends(user1.getId());
        List<Integer> friendsOfSecond = userStorage.getFriends(user2.getId());
        assertThat(friendsOfFirst.contains(user2.getId())).isTrue();
        assertThat(friendsOfSecond.contains(user1.getId())).isTrue();
    }

    @Test
    public void removeFriendFromOneUser() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user2.getId(), user1.getId());
        userStorage.removeFriend(user1.getId(), user2.getId());
        List<Integer> friendsOfFirst = userStorage.getFriends(user1.getId());
        List<Integer> friendsOfSecond = userStorage.getFriends(user2.getId());
        assertThat(friendsOfFirst.contains(user2.getId())).isFalse();
        assertThat(friendsOfSecond.contains(user1.getId())).isTrue();
    }

    @Test
    public void testCreateAndFindFilmById() {
        film1 = filmStorage.createFilm(film1);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.findFilmById(1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindAllFilms() {
        film1 = filmStorage.createFilm(film1);
        film2 = filmStorage.createFilm(film2);
        List<Film> films = filmStorage.findAllFilms();
        assertThat(films.contains(film1)).isTrue();
        assertThat(films.contains(film2)).isTrue();
    }

    @Test
    public void testRemoveFilm() {
        film1 = filmStorage.createFilm(film1);
        film2 = filmStorage.createFilm(film2);
        List<Film> films = filmStorage.findAllFilms();
        assertThat(films.contains(film1)).isTrue();
        assertThat(films.contains(film2)).isTrue();
        filmStorage.removeFilm(film2.getId());
        films = filmStorage.findAllFilms();
        assertThat(films).contains(film1);
        assertThat(films).doesNotContain(film2);
    }

    @Test
    public void updateFilm() {
        film1 = filmStorage.createFilm(film1);
        Optional<Film> filmBeforeUpdate = Optional.ofNullable(filmStorage.findFilmById(film1.getId()));
        assertThat(filmBeforeUpdate)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", film1.getId())
                                .hasFieldOrPropertyWithValue("name", "Film_1")
                                .hasFieldOrPropertyWithValue("description", "Description_1")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2020, 2, 12))
                                .hasFieldOrPropertyWithValue("duration", 120)
                );
        Film updatedFilm = Film.builder()
                .id(film1.getId())
                .name("NewFilm")
                .description("NewDescription")
                .releaseDate(LocalDate.of(1999, 12, 12))
                .duration(90)
                .build();
        updatedFilm.setMpa(new Mpa(4, "R"));
        updatedFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(3, "Мульфильм"))));
        filmStorage.updateFilm(updatedFilm);
        Optional<Film> filmafterUpdate = Optional.ofNullable(filmStorage.findFilmById(film1.getId()));
        assertThat(filmafterUpdate)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", film1.getId())
                                .hasFieldOrPropertyWithValue("name", "NewFilm")
                                .hasFieldOrPropertyWithValue("description", "NewDescription")
                                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, 12, 12))
                                .hasFieldOrPropertyWithValue("duration", 90)
                );
    }

    @Test
    public void addAdnRemoveLike() {
        user1 = userStorage.createUser(user1);
        film1 = filmStorage.createFilm(film1);
        filmStorage.addLike(user1.getId(), film1.getId());
        assertThat(filmStorage.userLikedFilm(user1.getId(), film1.getId())).isTrue();
        filmStorage.removeLike(user1.getId(), film1.getId());
        assertThat(filmStorage.userLikedFilm(user1.getId(), film1.getId())).isFalse();
    }

    @Test
    public void getPopularFilms() {
        user1 = userStorage.createUser(user1);
        user2 = userStorage.createUser(user2);
        film1 = filmStorage.createFilm(film1);
        film2 = filmStorage.createFilm(film2);
        filmStorage.addLike(user1.getId(), film1.getId());
        filmStorage.addLike(user1.getId(), film2.getId());
        filmStorage.addLike(user2.getId(), film2.getId());
        List<Film> popularFilms = filmStorage.getPopular(10);
        assertThat(popularFilms).containsExactly(film2, film1);
    }

    @Test
    public void getGenreById() {
        Optional<Genre> firstGenre = Optional.ofNullable(genresDbStorage.findById(1));
        assertThat(firstGenre)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("genreName", "Комедия")
                );
    }

    @Test
    public void getAllGenres() {
        List<Genre> genres = genresDbStorage.findAll();
        assert (genres).size() == 6;
    }

    @Test
    public void getMpaById() {
        Optional<Mpa> firstMpa = Optional.ofNullable(mpaDbStorage.findById(1));
        assertThat(firstMpa)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1)
                                .hasFieldOrPropertyWithValue("mpaName", "G")
                );
    }

    @Test
    public void getAllMpa() {
        List<Mpa> mpas = mpaDbStorage.findAll();
        assert (mpas).size() == 5;
    }

}
