package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private final LocalDate releaseDate = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            System.out.println("Такой фильм уже есть");
        } else {
            newFilmCheck(film);
            films.put(film.getId(), film);
            log.info("{}", film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            newFilmCheck(film);
            films.put(film.getId(), film);
            log.info("{}", film);
        } else {
            System.out.println("Такого фильма в списке нет");
        }
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Всего фильмов на портале: {}", films.size());
        return films.values();
    }

    private void newFilmCheck(Film film) throws ValidationException {
        if (film.getName().equals(null) || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(releaseDate)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной.");
        }
    }
}
