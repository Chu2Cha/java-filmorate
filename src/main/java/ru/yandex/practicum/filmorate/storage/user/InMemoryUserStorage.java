package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;
    private final List<User> users = new ArrayList<>();

    @Override
    public User create(User user) {
        User newUser = userValidation(user);
        newUser.setId(id++);
        users.add(newUser);
        log.info("Пользователь: {}", newUser);
        return newUser;
    }

    @Override
    public void delete(int userId) {
        boolean notFound = true;
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getId() == userId) {
                iterator.remove();
                users.remove(user);
                notFound = false;
                log.info("Пользователь с id {} удалён.", userId);
                break;
            }
        }
        if (notFound) {
            throw new ValidationException("Пользователь с id " + userId + " не найден.");
        }
    }

    @Override
    public User update(User user) {
        boolean noUsersForUpdate = true;
        for (int i = 0; i < users.size(); i++) {
            if (userValidation(user).equals(users.get(i))) {
                users.set(i, userValidation(user));
                noUsersForUpdate = false;
            }
        }
        if (noUsersForUpdate) {
            throw new ValidationException("Пользователь с id " + user.getId() + " не найден.");
        }
        log.info("Обновленный пользователь: {}", user);
        return user;
    }

    public List<User> findAll() {
        log.info("Количество пользователей: {}", users.size());
        return users;
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
