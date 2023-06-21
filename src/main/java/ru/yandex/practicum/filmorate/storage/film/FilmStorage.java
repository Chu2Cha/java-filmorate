package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    void removeFilm(int filmId);

    Film updateFilm(Film film);

    Film findFilmById(int id);

    List<Film> findAllFilms();

    void addLike(int userId, int filmId);

    void removeLike(int userId, int filmId);

    List<Film> getPopular(int count);

    boolean userLikedFilm(int userId, int filmId);
}
