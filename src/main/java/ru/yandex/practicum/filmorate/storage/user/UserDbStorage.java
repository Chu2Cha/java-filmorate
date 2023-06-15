package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        jdbcTemplate.update("INSERT INTO USERS VALUES ( 1, ?,?,?,? )",
                user.getEmail(),user.getLogin(),user.getName(),user.getBirthday());
        User returnUser = jdbcTemplate.query("SELECT USER_ID FROM USERS WHERE LOGIN = ?", new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
        user.setId(returnUser.getId());
        return user;
    }

    @Override
    public void removeUser(int userId) {

    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public User findUserById(int id) {
        return null;
    }

    @Override
    public void addFriend(int first, int second) {

    }

    @Override
    public void removeFriend(int first, int second) {

    }
}
