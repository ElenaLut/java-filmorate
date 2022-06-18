package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GeneratorId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Long, Film> films = new HashMap<>();
    private GeneratorId generatorId = new GeneratorId();

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Запрос на создание фильма {} отправлен", film);
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Такой фильм уже существует");
        }
        validateNewFilm(film);
        validateUniqName(film);
        film.setId(generatorId.generate());
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public void validateFilmId(long id) {
        if (!films.containsKey(id)) {
            log.error("Фильм с id={} не существует", id);
            throw new ObjectNotFoundException("Фильм с id=" + id + " не существует");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Запрос на обновление фильма {} отправлен", film);
        validateFilmId(film.getId());
        validateNewFilm(film);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        log.info("Фильм изменен: {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Всего фильмов на портале: {}", films.size());
        return films.values();
    }

    @Override
    public void validateUniqName(Film film) {
        for (Film f : films.values()) {
            if (film.getName().equals(f.getName())) {
                throw new ValidationException("Такой фильм уже существует под id " + f.getId());
            }
        }
    }

    @Override
    public void validateNewFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("название не может быть пустым.");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
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

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }
}