package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);
    void delete (int userId);
    User update (User user);

    List<User> findAll();
    User findUserById(int id);

    void addFriend(int first, int second);

    void removeFriend(int first, int second);
}
