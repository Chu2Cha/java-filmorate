package ru.yandex.practicum.filmorate.storage.Additional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpaDbStorage implements Additional<Mpa> {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findById(int id) {
        String sql = "SELECT * FROM MPA " +
                "WHERE MPA_ID =?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, id);
        if (srs.next()) {
            return mapRow(srs);
        } else
            return null;
    }

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * FROM MPA";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql);
        List<Mpa> mpas = new ArrayList<>();
        while (srs.next()) {
            mpas.add(mapRow(srs));
        }
        return mpas;
    }

    private Mpa mapRow(SqlRowSet srs) {
        return new Mpa(srs.getInt("MPA_ID"), srs.getString("MPA_NAME"));
    }
}
