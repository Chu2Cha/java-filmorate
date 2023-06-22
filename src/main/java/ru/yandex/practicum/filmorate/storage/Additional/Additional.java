package ru.yandex.practicum.filmorate.storage.Additional;

import java.util.List;

public interface Additional<T> {
    T findById(int id);
    List<T> findAll();
}
