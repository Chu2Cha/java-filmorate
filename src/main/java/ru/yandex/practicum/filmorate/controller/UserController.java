package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        return inMemoryUserStorage.update(user);
    }

    @DeleteMapping("/users/{userId}")
    public void delete(@PathVariable("userId") int userId) {
        inMemoryUserStorage.delete(userId);
    }


}
