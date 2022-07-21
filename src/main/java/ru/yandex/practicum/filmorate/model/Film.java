package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
//@Builder
public class Film {

    private final Set<Long> likes = new HashSet<>();

    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Rate mpa;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
}