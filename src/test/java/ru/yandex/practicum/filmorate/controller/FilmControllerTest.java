//package ru.yandex.practicum.filmorate.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.service.FilmService;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
//import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserStorage;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//class FilmControllerTest {
//    FilmController filmController;
//    UserController userController;
//    JdbcTemplate jdbcTemplate;
//    UserStorage userStorage;
//
//
//    @BeforeEach
//    public void start() {
//        userStorage = new UserDbStorage(jdbcTemplate);
//        FilmStorage filmStorage =  new FilmDbStorage(jdbcTemplate);
//        UserService userService = new UserService(userStorage);
//        FilmService filmService = new FilmService(filmStorage, userService);
//        filmController = new FilmController(filmService);
//        userController = new UserController(userService);
//    }
//    @Test
//    public void testFindUserById() {
//
//        Optional<User> userOptional = userStorage.findUserById(1);
//
//        assertThat(userOptional)
//                .isPresent()
//                .hasValueSatisfying(user ->
//                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
//                );
//    }
//    private Film createFilmForTest(String name, String description, int duration, LocalDate releaseDate) {
//        Film film = new Film();
//        film.setName(name);
//        film.setDescription(description);
//        film.setDuration(duration);
//        film.setReleaseDate(releaseDate);
//        return film;
//    }
//
//    @Test
//    void findAll() {
//        Film film1 = createFilmForTest("День выборов",
//                "Комедия Квартета И о выборах",
//                154,
//                LocalDate.of(2007, 10, 18));
//        Film film2 = createFilmForTest("День радио",
//                "Комедия Квартета И об одном дне из жизни радиостанции",
//                98,
//                LocalDate.of(2008, 3, 20));
//        filmController.create(film1);
//        assertEquals(1, filmController.findAll().size());
//        filmController.create(film2);
//        assertEquals(2, filmController.findAll().size());
//    }
//
//    @Test
//    void create() {
//        Film film1 = createFilmForTest("День выборов",
//                "Комедия Квартета И о выборах",
//                154,
//                LocalDate.of(2007, 10, 18));
//        filmController.create(film1);
//        assertEquals("[Film(id=1, name=День выборов, description=Комедия Квартета И о выборах, " +
//                "releaseDate=2007-10-18, duration=154, likedUsers=[])]", filmController.findAll().toString());
//    }
//
//    @Test
//    void updateFilm() {
//        Film film1 = createFilmForTest("День выборов",
//                "Комедия Квартета И о выборах",
//                154,
//                LocalDate.of(2007, 10, 18));
//        filmController.create(film1);
//        assertEquals("[Film(id=1, name=День выборов, description=Комедия Квартета И о выборах, " +
//                "releaseDate=2007-10-18, duration=154, likedUsers=[])]", filmController.findAll().toString());
//        film1.setName("День выборов 2");
//        film1.setReleaseDate(LocalDate.of(2016, 4, 4));
//        film1.setDuration(105);
//        filmController.update(film1);
//        assertEquals("[Film(id=1, name=День выборов 2, description=Комедия Квартета И о выборах, " +
//                "releaseDate=2016-04-04, duration=105, likedUsers=[])]", filmController.findAll().toString());
//    }
//
//    @Test
//    void shuldFailAndPassAfterUpdate() {
//        Film film = new Film();
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setName(" ");
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setName("Normal film name");
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setDescription("qGaudeamus igitur,\n" +
//                "Juvenes dum sumus! (bis)\n" +
//                "Post jucundam juventutem,\n" +
//                "Post molestam senectutem\n" +
//                "Nos habebit humus! (bis)\n" +
//                "Ubi sunt, qui ante nos\n" +
//                "In mundo fuere?\n" +
//                "Vadite ad superos,\n" +
//                "Transite ad inferos,\n" +
//                "Ubi jam fuere! (vel: Quos si vis videre.) (bis)\n" +
//                "Vita nostra brevis est,\n" +
//                "Brevi finietur.\n" +
//                "Venit mors velociter,\n" +
//                "Rapit nos atrociter,\n" +
//                "Nemini parcetur! (bis)\n" +
//                "Vivat Academia!\n" +
//                "Vivant professores!\n" +
//                "Vivat membrum quodlibet!\n" +
//                "Vivant membra quaelibet!\n" +
//                "Semper sint in flore! (bis)\n" +
//                "Vivant omnes virgines,\n" +
//                "Graciles (vel: faciles), formosae!\n" +
//                "Vivant et mulieres\n" +
//                "Tenerae, amabiles,\n" +
//                "Bonae, laboriosae! (bis)\n" +
//                "Vivat et respublica\n" +
//                "Et qui illam regit!\n" +
//                "Vivat nostra civitas,\n" +
//                "Maecenatum caritas,\n" +
//                "Quae nos hic protegit! (bis)\n" +
//                "Pereat tristitia,\n" +
//                "Pereant Dolores (osores)!\n" +
//                "Pereat diabolus,\n" +
//                "Quivis antiburschius\n" +
//                "Atque irrisores! (bis)");
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setDescription("Normal film description");
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setReleaseDate(LocalDate.of(1808, 12, 12));
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setReleaseDate(LocalDate.of(2023, 4, 1));
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setDuration(-5);
//        assertThrows(ValidationException.class, () -> filmController.create(film));
//        film.setDuration(55);
//        filmController.create(film);
//        assertEquals("[Film(id=1, name=Normal film name, description=Normal film description, " +
//                "releaseDate=2023-04-01, duration=55, likedUsers=[])]", filmController.findAll().toString());
//    }
//}