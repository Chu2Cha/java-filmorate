package ru.yandex.practicum.filmorate.model;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder(toBuilder = true)
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
//    private final Set<Integer> friends = new HashSet<>();
//
//    public void addFriend(int friendId) {
//        friends.add(friendId);
//    }
//
//    public void removeFriend(int friendId) {
//        friends.remove(friendId);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", email);
        values.put("LOGIN", login);
        values.put("NAME", name);
        values.put("BIRTHDAY", birthday);
        return values;
    }
}
