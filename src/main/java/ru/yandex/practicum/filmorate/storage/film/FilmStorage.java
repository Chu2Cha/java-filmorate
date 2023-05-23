package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);
    void removeFilm(int filmId);
    Film updateFilm(Film film);

    Film findFilmById(int id);

    List<Film> findAllFilms();
}
