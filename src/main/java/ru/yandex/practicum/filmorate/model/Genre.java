package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {
    private int id;
    private String name;
}