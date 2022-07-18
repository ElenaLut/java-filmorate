package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

@Slf4j
@Service
public class ValidationFilmService {

    private final int FILM_NAME_LENGTH = 200;
    private final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private InMemoryFilmStorage filmStorage;

    public void validateNewFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("название не может быть пустым.");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > FILM_NAME_LENGTH) {
            log.error("Максимальная длина описания — 200 символов: {}", film.getDescription().length());
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года: {}", film.getReleaseDate());
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть больше 0: {}", film.getDuration());
            throw new ValidationException("продолжительность фильма должна быть положительной.");
        }
    }

    public void validateFilmId(long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            log.error("Фильм с id={} не существует", id);
            throw new NotFoundException("Фильм с id=" + id + " не существует");
        }
    }

    public void validateUniqName(Film film) {
        for (Film f : filmStorage.getFilms().values()) {
            if (film.getName().equals(f.getName())) {
                throw new ValidationException("Такой фильм уже существует под id " + f.getId());
            }
        }
    }
}