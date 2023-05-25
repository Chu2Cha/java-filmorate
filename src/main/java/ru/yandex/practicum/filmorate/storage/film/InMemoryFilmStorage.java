package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {

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
        films.stream()
                .filter(f -> f.equals(film))
                .findFirst()
                .ifPresent(f -> films.set(films.indexOf(f), film));
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        Optional<Film> optionalFilm = films.stream()
                .filter(f -> f.getId() == id)
                .findFirst();
        return optionalFilm.orElse(null);
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films);
    }
}
