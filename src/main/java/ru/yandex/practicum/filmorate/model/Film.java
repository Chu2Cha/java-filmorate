package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
//    private Set<Integer> likedUsers = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

//    public void addLike(int userId) {
//        likedUsers.add(userId);
//    }
//
//    public void removeLike(int userId) {
//        likedUsers.remove(userId);
//    }
//
//    public int countLikes() {
//        return likedUsers.size();
//    }
}
