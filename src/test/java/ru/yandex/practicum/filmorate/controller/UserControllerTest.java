package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void start() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void createNewUserWithEmptyName() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@user.ru");
        user.setLogin("userLogin");
        user.setBirthday(LocalDate.now().minusYears(39));
        userController.create(user);
        assertEquals("userLogin", userController.getUser(1).getName());
    }


    private User createUserForTest(String name, LocalDate birthday, String login, String email) {
        User user = new User();
        user.setName(name);
        user.setBirthday(birthday);
        user.setLogin(login);
        user.setEmail(email);
        return user;
    }

    @Test
    void findAll() {
        User user1 = createUserForTest("Василиса", LocalDate.of(2015, 6, 11),
                "vasilisa", "vasya@cat.ru");
        User user2 = createUserForTest("Фасолька", LocalDate.of(2019, 7, 12),
                "redOne", "red@cat.ru");
        userController.create(user1);
        assertEquals(1, userController.findAll().size());
        userController.create(user2);
        assertEquals(2, userController.findAll().size());
    }

    @Test
    void create() {
        User user1 = createUserForTest("Василиса", LocalDate.of(2015, 6, 11),
                "vasilisa", "vasya@cat.ru");
        userController.create(user1);
        assertEquals("[User(id=1, email=vasya@cat.ru, login=vasilisa, " +
                "name=Василиса, birthday=2015-06-11, friends=[])]", userController.findAll().toString());
    }

    @Test
    void updateUser() {
        User user1 = createUserForTest("Василиса", LocalDate.of(2015, 6, 11),
                "vasilisa", "vasya@cat.ru");
        userController.create(user1);
        user1.setName("Новая Василиса");
        user1.setLogin("NewVasilisa");
        userController.update(user1);
        assertEquals("[User(id=1, email=vasya@cat.ru, login=NewVasilisa, " +
                "name=Новая Василиса, birthday=2015-06-11, friends=[])]", userController.findAll().toString());
    }

    @Test
    void shouldFailWhenCreateEmptyFieldsUserAndPassWhenEnterMailLoginBirthday() {
        User emptyUser = new User();
        assertThrows(ValidationException.class, () -> userController.create(emptyUser));
        emptyUser.setEmail("vasya@cat.ru");
        assertThrows(ValidationException.class, () -> userController.create(emptyUser));
        emptyUser.setLogin("Vasilisa");
        assertThrows(ValidationException.class, () -> userController.create(emptyUser));
        emptyUser.setBirthday(LocalDate.of(2015, 6, 11));
        userController.create(emptyUser);
        assertEquals("[User(id=1, email=vasya@cat.ru, login=Vasilisa, " +
                "name=Vasilisa, birthday=2015-06-11, friends=[])]", userController.findAll().toString());
    }
}