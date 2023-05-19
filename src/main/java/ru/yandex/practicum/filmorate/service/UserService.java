package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        User newUser = userValidation(user);
        userStorage.create(newUser);
        log.info("Пользователь: {}", newUser);
        return newUser;
    }

    public void delete(int userId) {
        if (userStorage.findUserById(userId) != null) {
            userStorage.delete(userId);
            log.info("Пользователь с id {} удалён.", userId);
        }
    }

    public User update(User user) {
        User updateUser = userValidation(user);
        if (userStorage.findUserById(updateUser.getId()) != null) {
            userStorage.update(updateUser);
            log.info("Обновленный пользователь: {}", updateUser);
        }
        return updateUser;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(int userId, int friendId) {
        if (checkFriendValidation(userId, friendId)) {
            if (userStorage.findUserById(userId).getFriends().contains(friendId)) {
                log.info("Пользователи {} и {} уже друзья!", userId, friendId);
            } else
            {
                userStorage.addFriend(userId, friendId);
                userStorage.addFriend(friendId, userId);
                log.info("Пользователи {} и {} подружились", userId, friendId);
            }
        }
    }

    public void removeFriend(int userId, int friendId) {
        if (checkFriendValidation(userId, friendId)) {
            if (!userStorage.findUserById(userId).getFriends().contains(friendId)) {
                log.info("Пользователи {} и {} не дружат.", userId, friendId);
            } else {
                userStorage.removeFriend(userId, friendId);
                userStorage.removeFriend(friendId, userId);
                log.info("Пользователи {} и {} пересати дружить", userId, friendId);
            }
        }
    }


    private boolean checkFriendValidation(int userId, int friendId) {
        if (userId == friendId) {
            log.info("Пользователю {} нельзя дружить самому с собой", userId);
            return false;
        }
        if (userStorage.findUserById(userId) == null) {
            log.info("Пользователь {} не найден", userId);
            return false;
        }
        if (userStorage.findUserById(friendId) == null) {
            log.info("Друг {} не найден", friendId);
            return false;
        }
        return true;
    }

    private User userValidation(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Почта не может быть пустой!");
        }
        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("Почта должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым!");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы!");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения должна быть раньше сегодняшнего дня.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return user;
    }

}
