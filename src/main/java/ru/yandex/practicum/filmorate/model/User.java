package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
   private int id; // целочисленный идентификатор — id;
   private String email; // электронная почта — email;
   private String login; // логин пользователя — login;
   private String name; // имя для отображения — name;
   private LocalDate birthday; // дата рождения — birthday.
}