package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        return inMemoryFilmStorage.create(film);
    }
    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        return inMemoryFilmStorage.update(film);
    }
    @DeleteMapping("/films/{filmId}")
    public void delete(@PathVariable("filmId") int filmId) {
        inMemoryFilmStorage.delete(filmId);
    }
}
