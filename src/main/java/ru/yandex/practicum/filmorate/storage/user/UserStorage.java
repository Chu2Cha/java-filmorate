package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User create(User user);
    void delete (int userId);
    User update (User user);
}
