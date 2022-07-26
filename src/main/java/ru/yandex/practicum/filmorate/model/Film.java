package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Rate mpa;
    private Set<Genre> genres;
}