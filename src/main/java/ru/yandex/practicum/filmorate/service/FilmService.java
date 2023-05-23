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
        if (findFilmById(updatedFilm.getId()) != null) {
            updatedFilm = filmStorage.updateFilm(updatedFilm);
            log.info("Обновленный фильм: {}", film);
        }
        return updatedFilm;
    }

    public void removeFilm(int id) {
        if (findFilmById(id) != null) {
            filmStorage.removeFilm(id);
            log.info("Фильм с id {} удалён.", id);
        }
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

    private Film findFilmById(int id) {
        if (filmStorage.findFilmById(id) != null) {
            return filmStorage.findFilmById(id);
        } else
            throw new NotFoundException("Фильм с id " + id + " не найден.");
    }

    public List<Film> findAll() {
        log.info("Количество фильмов: {}", filmStorage.findAllFilms().size());
        return filmStorage.findAllFilms();
    }

    public void addLike(int id, int userId) {
        User user = userService.getUser(userId);
        Film film = findFilmById(id);
        if (film.getLikedUsers().contains(userId)) {
            log.error("Пользователю {} нельзя ставить больше одного лайка фильму {}.", user.getName(), film.getName());
        } else {
            film.addLike(userId);
            log.info("Пользователь {} поставил лайк фильму {}.", user.getName(), film.getName());
        }
    }

    public Film getFilm(int id) {
        return findFilmById(id);
    }

    public void removeLike(int id, int userId) {
        User user = userService.getUser(userId);
        Film film = findFilmById(id);
        if (film.getLikedUsers().contains(userId)) {
            film.removeLike(userId);
            log.info("Пользователь {} удалил лайк фильму {}.", user.getName(), film.getName());
        } else {
            log.error("Пользователь {} не лайкал фильм {}.", user.getName(), film.getName());
        }
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> sortedFilms = new ArrayList<>(findAll());
        sortedFilms.sort(Comparator.comparingInt(Film::countLikes).reversed());
        int cut = Math.min(count, sortedFilms.size());
        return sortedFilms.subList(0, cut);
    }
}
