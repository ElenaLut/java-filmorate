package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                film.getRate() + 1
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

    private void addFilmGenre(Film film) {
        String newFilmGenre = "INSERT INTO film_genre VALUES (?, ?)";
        String newGenre = "INSERT INTO genre VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(newFilmGenre, film.getId(), genre.getId());
            jdbcTemplate.update(newGenre, genre.getId(), genre.getName());
        }
    }

    private void addFilmLikes(Film film) {
        String newFilmLike = "INSERT INTO film_likes VALUES (?, ?)";
        for (long i : film.getLikes()) {
            jdbcTemplate.update(newFilmLike, film.getId(), i);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new NotFoundException("Запрос пуст");
        }
        String filmChange = "UPDATE films " +
                "SET " +
                "film_name=?, " +
                "description=?, " +
                "duration=? " +
                "release_date=?" +
                "rate_id=?" +
                "WHERE film_id=?";
        jdbcTemplate.update(filmChange,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getRate() + 1,
                film.getId());
        deleteGenre(film);
        deleteLikes(film);
        if (!film.getGenres().isEmpty()) {
            addFilmGenre(film);
        }
        if (!film.getLikes().isEmpty()) {
            addFilmLikes(film);
        }
        log.info("Фильм изменен: {}", film);
        return film;
    }

    private void deleteGenre(Film film) {
        String genre = "DELETE FROM film_genre WHERE film_id=?";
        jdbcTemplate.update(genre, film.getId());
    }

    private void deleteLikes(Film film) {
        String likes = "DELETE FROM film_likes WHERE film_id=?";
        jdbcTemplate.update(likes, film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String query = "SELECT * FROM films";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(rs.getLong("film_id"), rs));
    }

    private Film makeFilm(long film_id, ResultSet rs) throws SQLException {
        String name = rs.getString("film_name");
        String description = rs.getString("description");
        int duration = rs.getInt("duration");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int rate_id = rs.getInt("rate_id");
        return new Film(film_id, name, description, release_date, duration, rate_id);
    }

    @Override
    public Film getFilmById(long id) {
        String query = "SELECT * FROM films WHERE film_id=?";
        List<Film> films = jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(id, rs), id);
        if (films.isEmpty()) {
            throw new NotFoundException("Объекта не существует");
        }
        return films.get(0);
    }

    public void addLike(long filmId, long userId) {
        String newFilmLike = "INSERT INTO film_likes VALUES (?, ?)";
        jdbcTemplate.update(newFilmLike, filmId, userId);
        log.info("Пользователь id={} поставил лайк фильму id={}", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        String likes = "DELETE FROM film_likes WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(likes, filmId, userId);
        log.info("Лайк пользователя id={} удален с фильма id={}", userId, filmId);
    }

    public List<Film> getTheMostPopularFilms(long count) {
        String films = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN " +
                "(SELECT " +
                "film_id, " +
                "COUNT(user_id) " +
                "FROM film_likes " +
                "GROUP BY film_id" +
                "ORDER BY COUNT(user_id)) AS likes" +
                "ON f.film_id = likes.film_id" +
                "ORDER BY likes.count DESC" +
                "LIMIT ?";
        return jdbcTemplate.query(films, (rs, rowNum) -> makeFilm(rs.getLong("film_id"), rs), count);
    }
}