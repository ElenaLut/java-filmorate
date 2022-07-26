package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}