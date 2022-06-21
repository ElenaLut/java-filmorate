package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.ValidationUserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final ValidationFilmService validationFilmService;
    private final ValidationUserService validationUserService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, ValidationFilmService validationFilmService,
                       ValidationUserService validationUserService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.validationFilmService = validationFilmService;
        this.validationUserService = validationUserService;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film getById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(long filmId, long userId) {
        log.info("Запрос от пользователя id={} на лайк к фильму id={} направлен", userId, filmId);
        Film film = filmStorage.getFilmById(filmId);
        validationUserService.validateUserId(userId);
        validationFilmService.validateFilmId(filmId);
        validateUsersHaveLike(userId, filmId);
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
    }

    public void validateUsersHaveLike(long userId, long filmId) {
        if (filmStorage.getFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь id={} уже ставил лайк фильму id={}", userId, filmId);
            throw new IllegalStateException("Пользователь id=" + userId + " уже ставил лайк фильму id=" + filmId);
        }
    }

    public void validateUsersHaveNotLike(long userId, long filmId) {
        if (!filmStorage.getFilms().get(filmId).getLikes().contains(userId)) {
            log.error("Пользователь id={} не ставил лайк фильму id={}", userId, filmId);
            throw new NotFoundException("Пользователь id=" + userId + " не ставил лайк фильму id=" + filmId);
        }
    }

    public void removeLike(long filmId, long userId) {
        log.info("Запрос на удаление лайка пользователя id={} с фильма id={} направлен", userId, filmId);
        Film film = filmStorage.getFilmById(filmId);
        validationUserService.validateUserId(userId);
        validationFilmService.validateFilmId(filmId);
        validateUsersHaveNotLike(userId, filmId);
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
        log.info("Лайк пользователя id={} удален с фильма id={}", userId, filmId);
    }

    public List<Film> getTheMostPopularFilms(long count) {
        log.info("Запрос на 10 наиболее популярных фильмов направлен");
        return filmStorage.getAllFilms().stream()
                .sorted((x, y)
                        -> Integer.compare(y.getLikes().size(), x.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}