package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;
    private final List<User> users = new ArrayList<>();

    @Override
    public User createUser(User user) {
        user.setId(id++);
        users.add(user);
        return user;
    }

    @Override
    public void removeUser(int userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    @Override
    public User updateUser(User user) {
        users.stream()
                .filter(u -> u.equals(user))
                .findFirst()
                .ifPresent(u -> users.set(users.indexOf(u), user));
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public User findUserById(int id) {
        Optional<User> optionalUser = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        return optionalUser.orElse(null);
    }

    @Override
    public void addFriend(int first, int second) {
        users.stream()
                .filter(u -> u.getId() == first)
                .findFirst()
                .ifPresent(u -> u.addFriend(second));
    }

    @Override
    public void removeFriend(int first, int second) {
        users.stream()
                .filter(u -> u.getId() == first)
                .findFirst()
                .ifPresent(u -> u.removeFriend(second));
    }
}
