package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Primary
@Component
public class FilmDbStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Film createFilm(Film film) {


        return null;
    }

    @Override
    public void removeFilm(int filmId) {

    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film findFilmById(int id) {
        return null;
    }

    @Override
    public List<Film> findAllFilms() {
        return null;
    }
}
