package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GeneratorId;
import ru.yandex.practicum.filmorate.service.film.ValidationFilmService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final ValidationFilmService validationFilmService = new ValidationFilmService();
    private final Map<Long, Film> films = new HashMap<>();
    private GeneratorId generatorId;

    @Autowired
    public InMemoryFilmStorage(GeneratorId generatorId) {
        this.generatorId = generatorId;
    }

    @Override
    public void validateFilmId(long id) {
        if (!films.containsKey(id)) {
            log.error("Фильм с id={} не существует", id);
            throw new NotFoundException("Фильм с id=" + id + " не существует");
        }
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
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Запрос на создание фильма {} отправлен", film);
        if (films.containsKey(film.getId())) {
            throw new IllegalStateException("Фильм с id=" + film.getId() + " уже существует.");
        }
        validationFilmService.validateNewFilm(film);
        validateUniqName(film);
        film.setId(generatorId.generate());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Запрос на обновление фильма {} отправлен", film);
        validateFilmId(film.getId());
        validationFilmService.validateNewFilm(film);
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
    public Film getFilmById(long id) {
        validateFilmId(id);
        return films.get(id);
    }
}