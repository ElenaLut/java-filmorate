package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rate;
import ru.yandex.practicum.filmorate.storage.filmgenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.rate.RateStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final RateStorage rateStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage,
                         RateStorage rateStorage, FilmGenreStorage filmGenreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.rateStorage = rateStorage;
        this.filmGenreStorage = filmGenreStorage;
    }

    @Override
    public Film addFilm(Film film) {
        if (film == null) {
            throw new NotFoundException("Запрос пуст");
        }
        String newFilm = "INSERT INTO films" +
                "(film_id, film_name, description, duration, release_date, rate_id) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(newFilm,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId()
        );
        if (!film.getGenres().isEmpty()) {
            addFilmGenre(film);
        }
        if (!film.getLikes().isEmpty()) {
            addFilmLikes(film);
        }
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new NotFoundException("Запрос пуст");
        }
        checkFilm(film.getId());
        String filmChange = "UPDATE films " +
                "SET " +
                "film_name=?, " +
                "description=?, " +
                "duration=?, " +
                "release_date=?, " +
                "rate_id=?" +
                "WHERE film_id=?";
        jdbcTemplate.update(filmChange,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        deleteGenre(film);
        deleteLikes(film);
        filmGenreStorage.updateGenresToFilm(film);
        if (!film.getLikes().isEmpty()) {
            addFilmLikes(film);
        }
        log.info("Фильм изменен: {}", film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String query = "SELECT * FROM films";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(rs.getLong("film_id"), rs));
    }

    @Override
    public Film getFilmById(long id) throws NotFoundException {
        String query = "SELECT * FROM films WHERE film_id=?";
        List<Film> films = jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(id, rs), id);
        if (films.isEmpty()) {
            throw new NotFoundException("Объекта не существует");
        }
        Film film = films.get(0);
        if (rateStorage.getRateById(film.getMpa().getId()) != null) {
            films.get(0).setMpa(rateStorage.getRateById(film.getMpa().getId()));
        }

        return film;
    }

    public void addLike(long filmId, long userId) {
        checkFilm(filmId);
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не обнаружен");
        }
        String newFilmLike = "INSERT INTO film_likes VALUES (?, ?)";
        jdbcTemplate.update(newFilmLike, filmId, userId);
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        checkFilm(filmId);
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не обнаружен");
        }
        String likes = "DELETE FROM film_likes WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(likes, filmId, userId);
        log.info("Лайк пользователя id={} удален с фильма id={}", userId, filmId);
    }

    public List<Film> getFilmsSortedByLikes(long count) {
        String films = "SELECT f.*\n" +
                "FROM films AS f\n" +
                "           LEFT JOIN\n" +
                "       (SELECT film_id, COUNT(user_id) AS COUNT\n" +
                "       FROM film_likes\n" +
                "       GROUP BY film_id\n" +
                "       ORDER BY COUNT(user_id)) AS likes\n" +
                "       ON f.film_id = likes.film_id\n" +
                "ORDER BY IFNULL(likes.COUNT, 0) DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(films, (rs, rowNum) -> makeFilm(rs.getLong("film_id"), rs), count);
    }

    private Film makeFilm(long film_id, ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(film_id);
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setDuration(rs.getInt("duration"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        Rate rate = new Rate();
        rate.setId(rs.getInt("rate_id"));
        film.setMpa(rate);
        film.setGenres((LinkedHashSet<Genre>) filmGenreStorage.findAllByFilmId(film_id));
        return film;
    }

    private void addFilmGenre(Film film) {
        checkFilm(film.getId());
        String newGenre = "INSERT INTO film_genre VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(newGenre, film.getId(), genre.getId());
        }
    }

    private void addFilmLikes(Film film) {
        checkFilm(film.getId());
        String newFilmLike = "INSERT INTO film_likes VALUES (?, ?)";
        for (long i : film.getLikes()) {
            jdbcTemplate.update(newFilmLike, film.getId(), i);
        }
    }

    private void deleteGenre(Film film) {
        checkFilm(film.getId());
        String genre = "DELETE FROM film_genre WHERE film_id=?";
        jdbcTemplate.update(genre, film.getId());
    }

    private void deleteLikes(Film film) {
        checkFilm(film.getId());
        String likes = "DELETE FROM film_likes WHERE film_id=?";
        jdbcTemplate.update(likes, film.getId());
    }

    private void checkFilm(long id) {
        if (getFilmById(id) == null) {
            throw new NotFoundException("Фильм с id " + id + " не обнаружен");
        }
    }
}