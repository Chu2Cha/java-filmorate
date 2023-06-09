package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;


    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film createFilm(Film film) {
        Film newFilm = filmValidation(film);
        filmStorage.createFilm(newFilm);
        log.info("Фильм: {}", newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmValidation(film);
        findFilmById(updatedFilm.getId());      // проверяет наличие id для upgrade в storage.
        updatedFilm = filmStorage.updateFilm(updatedFilm);
        log.info("Обновленный фильм: {}", film);
        return updatedFilm;
    }

    public void removeFilm(int id) {
        findFilmById(id);
        filmStorage.removeFilm(id);
        log.info("Фильм с id {} удалён.", id);
    }

    public List<Film> findAll() {
        List<Film> allFilms = new ArrayList<>(filmStorage.findAllFilms());
        log.info("Количество фильмов: {}", allFilms.size());
        return allFilms;
    }

    public void addLike(int filmId, int userId) {
        User user = userService.getUser(userId);
        Film film = findFilmById(filmId);
        if (filmStorage.userLikedFilm(userId, filmId)) {
            log.error("Пользователю {} нельзя ставить больше одного лайка фильму {}.", user.getName(), film.getName());
        } else {
            filmStorage.addLike(userId, filmId);
            log.info("Пользователь {} поставил лайк фильму {}.", user.getName(), film.getName());
        }
    }

    public Film getFilm(int id) {
        return findFilmById(id);
    }

    public void removeLike(int filmId, int userId) {
        User user = userService.getUser(userId);
        Film film = findFilmById(filmId);
        if (filmStorage.userLikedFilm(userId, filmId)) {
            filmStorage.removeLike(userId, filmId);
            log.info("Пользователь {} удалил лайк фильму {}.", user.getName(), film.getName());
        } else {
            log.error("Пользователь {} не лайкал фильм {}.", user.getName(), film.getName());
        }
    }

    public List<Film> getPopularFilms(int count) {
        return new ArrayList<>(filmStorage.getPopular(count));
    }

    private Film findFilmById(int id) {
        return Optional.ofNullable(filmStorage.findFilmById(id))
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден."));
    }

    private Film filmValidation(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            throw new ValidationException("У фильма должно быть описание, но не длиннее 200 символов.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть пустой и должна быть не раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return film;
    }
}
