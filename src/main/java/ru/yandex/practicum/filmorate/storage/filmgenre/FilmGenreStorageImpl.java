package ru.yandex.practicum.filmorate.storage.filmgenre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Component
public class FilmGenreStorageImpl implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genre> findAllByFilmId(long id) {
        final String query = "SELECT * FROM film_genre JOIN genre  on genre.genre_id = film_genre.genre_id " +
                "WHERE film_id = ? ORDER BY 1 DESC";
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(query, id);
        while (rs.next()) {
            genres.add(new Genre(rs.getInt("genre_id"),
                    rs.getString("genre_name")));
        }
        return genres;
    }

    @Override
    public void addGenreToFilm(long id, Genre genre) {
        String newGenre = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(newGenre, id, genre.getId());
    }

    @Override
    public void updateGenresToFilm(Film film) {
        if (film.getGenres() == null) {
            final String sql = "DELETE FROM film_genre WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getId());
            return;
        }
        final String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
        for (Genre genre : film.getGenres()) {
            addGenreToFilm(film.getId(), genre);
        }
    }
}