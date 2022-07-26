package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Rate {

    private int id;
    private String name;
}