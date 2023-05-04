package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private int id =1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> findAll() {
        log.debug("Количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        for (User addedUser : users) {
            if (user.equals(addedUser)) {
                throw new ValidationException("Пользователь с таким email уже существует!");
            }
        }
        User newUser = userValidation(user);
        newUser.setId(id++);
        users.add(newUser);
        log.debug("Пользователь: {}", newUser);
        return newUser;
    }

    @PutMapping("/users")
    public User put(@RequestBody User user) {
        boolean checkUserforUpdate = false;
        for (int i = 0; i < users.size(); i++) {
            if(userValidation(user).equals(users.get(i))){
                users.set(i, userValidation(user));
                checkUserforUpdate = true;
            }
        }
        if(!checkUserforUpdate){
            throw new ValidationException("Пользователь с id " + user.getId() + " не найден.");
        }
        log.debug("Обновленный пользователь: {}", user);
        return user;
    }

    private User userValidation(User user){
        if(user.getEmail().isEmpty()){
            throw new ValidationException("Почта не может быть пустой!");
        }
        if(!(user.getEmail().contains("@"))){
            throw new ValidationException("Почта должна содержать символ @.");
        }
        if(user.getLogin().isEmpty()){
            throw new ValidationException("Логин не может быть пустым!");
        }
        if(user.getLogin().contains(" ")){
            throw new ValidationException("Логин не может содержать пробелы!");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Дата рождения должна быть раньше сегодняшнего дня.");
        }
        if(user.getName() == null || user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        return user;
    }

}
