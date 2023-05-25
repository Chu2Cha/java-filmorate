package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        User newUser = userValidation(user);
        newUser = userStorage.createUser(newUser);
        log.info("Пользователь: {}", newUser);
        return newUser;
    }

    public User updateUser(User user) {
        User updatedUser = userValidation(user);
        findUserById(updatedUser.getId());
        updatedUser = userStorage.updateUser(updatedUser);
        log.info("Обновленный пользователь: {}", updatedUser);
        return updatedUser;
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>(userStorage.findAllUsers());
        log.info("Количество пользователей: {}", users.size());
        return users;
    }

    public User getUser(int id) {
        return findUserById(id);
    }

    public void removeUser(int id) {
        findUserById(id);
        userStorage.removeUser(id);
        log.info("Пользователь с id {} удалён.", id);
    }

    public void addFriend(int userId, int friendId) {
        if (checkFriendValidation(userId, friendId)) {
            if (findUserById(userId).getFriends().contains(friendId)) {
                log.info("Пользователи {} и {} уже друзья!", userId, friendId);
            } else {
                userStorage.addFriend(userId, friendId);
                userStorage.addFriend(friendId, userId);
                log.info("Пользователи {} и {} подружились", userId, friendId);
            }
        }
    }

    public void removeFriend(int userId, int friendId) {
        if (checkFriendValidation(userId, friendId)) {
            if (!findUserById(userId).getFriends().contains(friendId)) {
                log.info("Пользователи {} и {} не дружат.", userId, friendId);
            } else {
                userStorage.removeFriend(userId, friendId);
                userStorage.removeFriend(friendId, userId);
                log.info("Пользователи {} и {} пересати дружить", userId, friendId);
            }
        }
    }

    public List<User> getFriendList(int id) {
        return findUserById(id).getFriends().stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> getFriendsCommonList(int id, int otherId) {
        User firstUser = findUserById(id);
        User secondUser = findUserById(otherId);
        Set<Integer> intersection = new HashSet<>(firstUser.getFriends());
        intersection.retainAll(secondUser.getFriends());
        return intersection.stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    private User findUserById(int id) {
        return Optional.ofNullable(userStorage.findUserById(id))
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден."));
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

    private boolean checkFriendValidation(int userId, int friendId) {
        if (userId == friendId) {
            log.error("Пользователю {} нельзя дружить самому с собой", userId);
            throw new ValidationException("Пользователю " + userId + " нельзя дружить самому с собой");
        }
        if (findUserById(userId) == null) {
            log.error("Пользователь {} не найден", userId);
            throw new NotFoundException("Пользователь" + userId + " не найден");
        }
        if (findUserById(friendId) == null) {
            log.error("Друг {} не найден", friendId);
            throw new NotFoundException("Друг" + friendId + " не найден");
        }
        return true;
    }
}
