package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Rate mpa;
    private Set<Long> likes = new HashSet<>();
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
}