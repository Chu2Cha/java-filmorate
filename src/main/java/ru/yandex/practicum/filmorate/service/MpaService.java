package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Additional.MpaDbStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Mpa findById(int id) {
        return Optional.ofNullable(mpaDbStorage.findById(id))
                .orElseThrow(() -> new NotFoundException("Рейтинг с id " + id + " не найден."));
    }

    public List<Mpa> findAll() {
        List<Mpa> allMpa = new ArrayList<>(mpaDbStorage.findAll());
        log.info("Количество рейтингов: {}", allMpa.size());
        return allMpa;
    }
}
