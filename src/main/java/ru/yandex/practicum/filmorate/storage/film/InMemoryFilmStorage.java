package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private int id = 1;

    private final List<Film> films = new ArrayList<>();

    @Override
    public Film create(Film film) {
        Film newFilm = filmValidation(film);
        newFilm.setId(id++);
        films.add(newFilm);
        log.info("Фильм: {}", newFilm);
        return newFilm;
    }

    @Override
    public void delete(int filmId) {
        boolean notFound = true;
        Iterator<Film> iterator = films.iterator();
        while (iterator.hasNext()) {
            Film film = iterator.next();
            if (film.getId() == filmId) {
                iterator.remove();
                films.remove(film);
                notFound = false;
                log.info("Фильм с id {} удалён.", filmId);
                break;
            }
        }
        if (notFound) {
            throw new ValidationException("Фильм с id " + filmId + " не найден.");
        }
    }

    @Override
    public Film update(Film film) {
        boolean noFilmForUpdate = true;
        for (int i = 0; i < films.size(); i++) {
            if (filmValidation(film).equals(films.get(i))) {
                films.set(i, filmValidation(film));
                noFilmForUpdate = false;
            }
        }
        if (noFilmForUpdate) {
            throw new ValidationException("Фильм с id " + film.getId() + " не найден.");
        }
        log.info("Обновленный фильм: {}", film);
        return film;
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

    public List<Film> findAll() {
        log.info("Количество фильмов: {}", films.size());
        return films;
    }
}
