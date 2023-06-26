package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Primary
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        return user;
    }

    @Override
    public void removeUser(int userId) {
        jdbcTemplate.update("DELETE FROM USERS WHERE USER_ID = ?", userId);
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        String sqlQuery = "SELECT * FROM USERS";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<User> users = new ArrayList<>();
        while (srs.next()) {
            users.add(mapRowToUser(srs));
        }
        return users;
    }

    @Override
    public User findUserById(int id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            return mapRowToUser(srs);
        } else {
            return null;
        }
    }

    @Override
    public void addFriend(int first, int second) {
        List<Map<String, Object>> firstList =
                jdbcTemplate.queryForList("SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?",
                        first, second);
        List<Map<String, Object>> secondList =
                jdbcTemplate.queryForList("SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?",
                        second, first);
        if (firstList.isEmpty() && secondList.isEmpty()) {
            jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID, FRIEND_STATUS) VALUES (?, ?, ?)",
                    first, second, false);
        } else {
            jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID, FRIEND_STATUS) VALUES (?, ?, ?)",
                    first, second, true);
            jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID, FRIEND_STATUS) VALUES (?, ?, ?)",
                    second, first, true);
        }
    }

    @Override
    public void removeFriend(int first, int second) {
        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", first, second);
        jdbcTemplate.update("UPDATE FRIENDS SET FRIEND_STATUS= ? WHERE USER_ID = ? AND  FRIEND_ID = ?",
                false, second, first);
    }

    @Override
    public List<Integer> getFriends(int userId) {
        String sqlQuery = "SELECT * FROM USERS INNER JOIN FRIENDS F on USERS.USER_ID = F.FRIEND_ID WHERE F.USER_ID = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        List<Integer> friends = new ArrayList<>();
        while (srs.next()) {
            friends.add(mapRowToUser(srs).getId());
        }
        return friends;
    }


    private static User mapRowToUser(SqlRowSet srs) {
        return User.builder()
                .id(srs.getInt("USER_ID"))
                .name(srs.getString("NAME"))
                .login(srs.getString("LOGIN"))
                .email(srs.getString("EMAIL"))
                .birthday(Objects.requireNonNull(srs.getDate("BIRTHDAY")).toLocalDate())
                .build();
    }
}
