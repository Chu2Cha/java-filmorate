package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film create(Film film);
    void delete (int filmId);
    Film update (Film film);

}
