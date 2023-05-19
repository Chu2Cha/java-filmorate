package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;
    private final List<User> users = new ArrayList<>();

    @Override
    public User create(User user) {
        user.setId(id++);
        users.add(user);
        return user;
    }

    @Override
    public void delete(int userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    @Override
    public User update(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (user.equals(users.get(i))) {
                users.set(i, user);
                break;
            }
        }
        return user;
    }

    public List<User> findAll() {
        log.info("Количество пользователей: {}", users.size());
        return users;
    }
    public User findUserById(int id){
        for(User user : users){
            if (user.getId()==id)
                return user;
            else
                log.info("Пользователь с id " + id + " не найден.");
        }
        return null;
    }

    @Override
    public void addFriend(int first, int second) {
        for (User user: users){
            if(user.getId()==first){
                user.addFriend(second);
                break;
            }
        }
    }

    @Override
    public void removeFriend(int first, int second) {
        for (User user: users){
            if(user.getId()==first){
                user.removeFriend(second);
            }
        }
    }
}
