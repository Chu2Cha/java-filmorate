package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User createUser(User user);

    void removeUser(int userId);

    User updateUser(User user);

    List<User> findAllUsers();

    User findUserById(int id);

    void addFriend(int first, int second);

    void removeFriend(int first, int second);

    List<Integer> getFriends(int userId);
}
