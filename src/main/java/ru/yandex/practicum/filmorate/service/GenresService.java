package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Additional.GenresDbStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GenresService {
    private final GenresDbStorage genresDbStorage;

    public GenresService(GenresDbStorage genresDbStorage) {
        this.genresDbStorage = genresDbStorage;
    }
    public Genre findById(int id){
        return Optional.ofNullable(genresDbStorage.findById(id))
                .orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден."));
    }

    public List<Genre> findAll() {
        List<Genre> allGenres = new ArrayList<>(genresDbStorage.findAll());
        log.info("Количество рейтингов: {}", allGenres.size());
        return allGenres;
    }
}
