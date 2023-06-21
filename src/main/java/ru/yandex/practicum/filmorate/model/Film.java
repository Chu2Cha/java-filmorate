package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder(toBuilder = true)
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Genre> genres;
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

    public Set<Genre> getGenres() { //для правильной сортировки во воремя PUT. Правда, я не понял, зачем это нужно
        try{
            Set<Genre> genresFromFilm = new TreeSet<>(new GenreComparator());
            genresFromFilm.addAll(genres);
            return genresFromFilm;
        } catch (NullPointerException e){ //если у film нет вообще genres, падает NPE
            return new HashSet<>();
        }
    }

    private static class GenreComparator implements Comparator<Genre> {
        @Override
        public int compare(Genre o1, Genre o2) {
            return Integer.compare(o1.getId(), o2.getId());
        }
    }
}
