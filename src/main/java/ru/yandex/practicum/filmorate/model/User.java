package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private List<Long> friends = new ArrayList<>();
}