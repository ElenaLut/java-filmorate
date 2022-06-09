package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id; //целочисленный идентификатор — id;
    private String name; //название — name;
    private String description; //описание — description;
    private LocalDate releaseDate;//дата релиза — releaseDate;
    private int duration; //продолжительность фильма — duration.
}
