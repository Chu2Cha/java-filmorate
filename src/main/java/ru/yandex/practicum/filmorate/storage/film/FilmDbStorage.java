package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.*;

@Primary
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        String query =
                "INSERT INTO FILM(FILM_NAME, DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION, MPA_ID) " +
                        "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        updateGenre(film);
        return film;
    }


    @Override
    public void removeFilm(int filmId) {
        jdbcTemplate.update("DELETE FROM FILM WHERE FILM_ID = ?", filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILM SET FILM_NAME = ?, DESCRIPTION = ?, FILM_RELEASE_DATE = ?, " +
                "FILM_DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(
                sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()
        );
        updateGenre(film);
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        String sql = "SELECT * FROM FILM " +
                "JOIN MPA M ON M.MPA_ID = FILM.MPA_ID " +
                "WHERE FILM.FILM_ID =?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, id);
        if (srs.next()) {
            return mapRowToFilm(srs);
        } else
            return null;
    }

    @Override
    public List<Film> findAllFilms() {
        String sql = "SELECT * FROM FILM " +
                "JOIN MPA M ON M.MPA_ID = FILM.MPA_ID ";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
        List<Film> films = new ArrayList<>();
        while (srs.next()) {
            films.add(mapRowToFilm(srs));
        }
        return films;
    }

    @Override
    public void addLike(int userId, int filmId) {
        String sql = "INSERT INTO FILM_USER_RATING VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public void removeLike(int userId, int filmId) {
        jdbcTemplate.update("DELETE FROM FILM_USER_RATING WHERE USER_ID =? AND FILM_ID = ?", userId, filmId);
    }

    @Override
    public List<Film> getPopular(int count) {

        String sql = "SELECT F.*, M.*, COUNT(FUR.FILM_ID) FROM FILM F LEFT " +
                "JOIN FILM_USER_RATING FUR on F.FILM_ID = FUR.FILM_ID " +
                "JOIN MPA M on M.MPA_ID = F.MPA_ID " +
                "group by F.FILM_ID " +
                "ORDER BY COUNT(FUR.FILM_ID) DESC " +
                "LIMIT ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, count);
        List<Film> films = new ArrayList<>();
        while (srs.next()) {
            films.add(mapRowToFilm(srs));
        }
        return films;
    }

    @Override
    public boolean userLikedFilm(int userId, int filmId) {
        String sql = "SELECT * FROM FILM_USER_RATING WHERE USER_ID = ? AND FILM_ID = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        return srs.next();
    }

    private Film mapRowToFilm(SqlRowSet srs) {
        Mpa mpa = new Mpa(srs.getInt("MPA_ID"), srs.getString("MPA_NAME"));
        Set<Genre> genres = getGenresFromFilm(srs.getInt("FILM_ID"));
        return Film.builder()
                .id(srs.getInt("FILM_ID"))
                .name(srs.getString("FILM_NAME"))
                .description(srs.getString("DESCRIPTION"))
                .releaseDate(Objects.requireNonNull(srs.getDate("FILM_RELEASE_DATE")).toLocalDate())
                .duration(srs.getInt("FILM_DURATION"))
                .mpa(mpa)
                .genres(genres)
                .build();
    }

    private Set<Genre> getGenresFromFilm(int filmId) {
        Set<Genre> genres = new HashSet<>();
        String sql = "SELECT * FROM FILM_GENRE FG JOIN GENRE G on G.GENRE_ID = FG.GENRE_ID WHERE FG.FILM_ID = ? " +
                "ORDER BY G.GENRE_ID ASC";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, filmId);
        while ((srs.next())) {
            genres.add(new Genre(srs.getInt("GENRE_ID"), srs.getString("GENRE_NAME")));
        }
        return genres;
    }


    private void updateGenre(Film film) {
        Set<Genre> genresFromFilm = getGenresFromFilm(film.getId());
        if (genresFromFilm.isEmpty()) {
            doNothing(); //чтобы не ставить плохо читаемое отрицание в выражении
        } else {
            String deleteSql = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
            jdbcTemplate.update(deleteSql, film.getId());
        }
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            doNothing();
        } else {
            for (Genre genre : film.getGenres()) {
                String insertSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
                jdbcTemplate.update(insertSql, film.getId(), genre.getId());
            }
        }
    }
    private void doNothing(){}

}
