package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Mpa {
    private int id;
    @JsonProperty("name")
    private String mpaName;

    public Mpa(int id, String mpaName) {
        this.id = id;
        this.mpaName = mpaName;
    }
}
