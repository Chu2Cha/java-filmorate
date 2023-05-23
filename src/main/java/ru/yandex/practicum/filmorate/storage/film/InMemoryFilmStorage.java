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
        for (int i = 0; i < films.size(); i++) {
            if (film.equals(films.get(i))) {
                films.set(i, film);
            }
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
}
