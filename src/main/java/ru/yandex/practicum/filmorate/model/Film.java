package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
//@Builder
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private Set<Genre> genres = new HashSet<>();
    private final Set<Long> likes = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.name = name;
        this.duration = duration;
        this.rate = rate;
    }

}