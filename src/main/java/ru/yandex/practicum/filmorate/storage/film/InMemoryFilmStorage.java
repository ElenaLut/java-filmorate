package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.GeneratorFilmId;
import ru.yandex.practicum.filmorate.service.film.ValidationFilmService;
import ru.yandex.practicum.filmorate.service.user.ValidationUserService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final ValidationFilmService validationFilmService = new ValidationFilmService();
    private final ValidationUserService validationUserService = new ValidationUserService();
    private final Map<Long, Film> films = new HashMap<>();

    @Autowired
    private GeneratorFilmId generatorId = new GeneratorFilmId();

    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new IllegalStateException("Фильм с id=" + film.getId() + " уже существует.");
        }
        validationFilmService.validateUniqName(film);
        film.setId(generatorId.generate());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validationFilmService.validateFilmId(film.getId());
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
        validationFilmService.validateFilmId(id);
        return films.get(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        validationUserService.validateUserId(userId);
        validationFilmService.validateFilmId(filmId);
        validateUsersHaveLike(userId, filmId);
        film.getLikes().add(userId);
        updateFilm(film);
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
    }

    public void validateUsersHaveLike(long userId, long filmId) {
        if (getFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь id={} уже ставил лайк фильму id={}", userId, filmId);
            throw new IllegalStateException("Пользователь id=" + userId + " уже ставил лайк фильму id=" + filmId);
        }
    }

    public void validateUsersHaveNotLike(long userId, long filmId) {
        if (!getFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь id={} не ставил лайк фильму id={}", userId, filmId);
            throw new NotFoundException("Пользователь id=" + userId + " не ставил лайк фильму id=" + filmId);
        }
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        validationUserService.validateUserId(userId);
        validationFilmService.validateFilmId(filmId);
        validateUsersHaveNotLike(userId, filmId);
        film.getLikes().remove(userId);
        updateFilm(film);
        log.info("Лайк пользователя id={} удален с фильма id={}", userId, filmId);
    }

    @Override
    public List<Film> getFilmsSortedByLikes(long count) {
        log.info("Запрос на 10 наиболее популярных фильмов направлен");
        return getAllFilms().stream()
                .sorted((x, y)
                        -> Integer.compare(y.getLikes().size(), x.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}