package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        User newUser = userValidation(user);
        newUser = userStorage.create(newUser);
        log.info("Пользователь: {}", newUser);
        return newUser;
    }

    public User updateUser(User user) {
        User updatedUser = userValidation(user);
        if (findUserById(updatedUser.getId()) != null) {
            updatedUser = userStorage.update(updatedUser);
            log.info("Обновленный пользователь: {}", updatedUser);
        }
        return updatedUser;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User getUser(int id) {
        return userValidation(findUserById(id));
    }

    public void deleteUser(int id) {
        if (findUserById(id) != null) {
            userStorage.delete(id);
            log.info("Пользователь с id {} удалён.", id);
        }
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
        User user = userValidation(findUserById(id));
        List<User> friends = new ArrayList<>();
        for (Integer friendId : user.getFriends()) {
            friends.add(findUserById(friendId));
        }
        return friends;
    }

    public List<User> getFriendsCommonSet(int id, int otherId) {
        User firstUser = userValidation(findUserById(id));
        User secondUser = userValidation(findUserById(otherId));
        Set<Integer> intersection = new HashSet<>(firstUser.getFriends());
        intersection.retainAll(secondUser.getFriends());
        List<User> friends = new ArrayList<>();
        for (Integer friendId : intersection) {
            friends.add(findUserById(friendId));
        }
        return friends;
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
            throw new ValidationException("Пользователю" + userId +" нельзя дружить самому с собой");
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

    private User findUserById(int id){
        if(userStorage.findUserById(id)!=null){
            return userStorage.findUserById(id);
        } else
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
    }
}
