package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneratorId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private GeneratorId countId = new GeneratorId();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Запрос на создание фильма {} отправлен", film);
        if (films.containsKey(film.getId()) || checkName(film)) {
            throw new ValidationException("Такой фильм уже существует");
        } else {
            film.setId(countId.generate());
            newFilmCheck(film);
            films.put(film.getId(), film);
            log.info("{}", film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Запрос на обновление фильма {} отправлен", film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого фильма в списке нет");
        }
            newFilmCheck(film);
            films.put(film.getId(), film);
            log.info("Фильм изменен: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Всего фильмов на портале: {}", films.size());
        return films.values();
    }

    private boolean checkName(Film film) {
        boolean nameInBase = false;
        for (Film f : films.values()) {
            if (film.getName().equals(f.getName())) {
                nameInBase = true;
            }
        }
        return nameInBase;
    }

    private void newFilmCheck(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("название не может быть пустым.");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов: {}", film.getDescription().length());
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года: {}", film.getReleaseDate());
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть больше 0: {}", film.getDuration());
            throw new ValidationException("продолжительность фильма должна быть положительной.");
        }
    }
}
