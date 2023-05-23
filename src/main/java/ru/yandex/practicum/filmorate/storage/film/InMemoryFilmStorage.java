package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private int id = 1;

    private final List<Film> films = new ArrayList<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(id++);
        films.add(film);
        return film;
    }

    @Override
    public void removeFilm(int filmId) {
        films.removeIf(film -> film.getId() == filmId);
    }

    @Override
    public Film updateFilm(Film film) {
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

        return film;
    }

    @Override
    public Film findFilmById(int id) {
        for (Film film : films) {
            if (film.getId() == id) {
                return film;
            }
        }
        return null;
    }

    @Override
    public List<Film> findAllFilms() {
        return films;
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
