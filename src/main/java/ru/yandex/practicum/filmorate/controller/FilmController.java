package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private int id = 1;

    private final List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        Film newFilm = filmValidation(film);
        newFilm.setId(id++);
        films.add(newFilm);
        log.debug("Фильм: {}", newFilm);
        return newFilm;
    }

    @PutMapping("/films")
    public Film put(@RequestBody Film film) {
        boolean checkFilmForUpdate = false;
        for (int i = 0; i < films.size(); i++) {
            if (filmValidation(film).equals(films.get(i))) {
                films.set(i, filmValidation(film));
                checkFilmForUpdate = true;
            }
        }
        if (!checkFilmForUpdate) {
            throw new ValidationException("Фильм с id " + film.getId() + " не найден.");
        }
        log.debug("Обновленный фильм: {}", film);
        return film;
    }

    private Film filmValidation(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания фильма — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return film;
    }


}
