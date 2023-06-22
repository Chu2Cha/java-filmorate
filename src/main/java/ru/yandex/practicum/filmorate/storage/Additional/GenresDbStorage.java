package ru.yandex.practicum.filmorate.storage.Additional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenresDbStorage implements Additional<Genre> {
    private final JdbcTemplate jdbcTemplate;

    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findById(int id) {
        String sql = "SELECT * FROM GENRE " +
                "WHERE GENRE_ID =?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, id);
        if (srs.next()) {
            return mapRow(srs);
        } else
            return null;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM GENRE";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
        List<Genre> genres = new ArrayList<>();
        while (srs.next()) {
            genres.add(mapRow(srs));
        }
        return genres;
    }

    private Genre mapRow(SqlRowSet srs) {
        return new Genre(srs.getInt("GENRE_ID"), srs.getString("GENRE_NAME"));
    }
}
