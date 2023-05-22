package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm (Film film){
        Film newFilm = filmValidation(film);
        filmStorage.create(newFilm);
        log.info("Фильм: {}", newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film){
        Film updatedFilm = filmValidation(film);
        updatedFilm=filmStorage.update(updatedFilm);
        log.info("Обновленный фильм: {}", film);
        return updatedFilm;
    }

    public void removeFilm(Film film){

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
