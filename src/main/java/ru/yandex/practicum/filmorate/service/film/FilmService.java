package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final ValidationFilmService validationFilmService = new ValidationFilmService();
    private final FilmStorage filmStorage;

    private GeneratorFilmId generatorFilmIdId = new GeneratorFilmId();

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        log.info("Запрос на создание фильма {} отправлен", film);
        validationFilmService.validateNewFilm(film);
        film.setId(generatorFilmIdId.generate());
        return filmStorage.addFilm(film);
    }

    public Film getById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        log.info("Запрос на обновление фильма {} отправлен", film);
        validationFilmService.validateNewFilm(film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(long filmId, long userId) {
        log.info("Запрос от пользователя id={} на лайк к фильму id={} направлен", userId, filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.info("Запрос на удаление лайка пользователя id={} с фильма id={} направлен", userId, filmId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getTheMostPopularFilms(long count) {
        return filmStorage.getTheMostPopularFilms(count);
    }
}