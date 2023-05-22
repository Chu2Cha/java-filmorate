package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") int userId) {
        userService.delete(userId);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") int userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userService.addFriend(id, friendId);
    }
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        userService.removeFriend(id, friendId);
    }
    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") int id){
        return userService.getFriendList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUserCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId){
        return userService.getFriendsCommonSet(id, otherId);
    }

}
