package ru.yandex.practicum.filmorate.storage.filmgenre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorageImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmGenreStorageImpl implements FilmGenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmGenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Genre> findAllByFilmId(long id){
        final String query = "SELECT * FROM film_genre JOIN genre  on genre.genre_id = film_genre.genre_id " +
                "WHERE film_id = ? ORDER BY 1 DESC";
        Set<Genre> genres = new HashSet<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(query, id);
        while (rs.next()) {
            genres.add(new Genre(rs.getInt("genre_id"),
                    rs.getString("genre_name")));
        }
       /* if (genres.isEmpty()) {
            throw new NotFoundException("Такого рейтинга не существует");
        }*/
        return genres;
    }

@Override
public void addGenreToFilm(long id, Genre genre) {
    String newGenre = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
    jdbcTemplate.update(newGenre, id, genre.getId());
    }
@Override
    public void updateGenresToFilm (Film film) {
    if (film.getGenres() != null & film.getGenres().isEmpty()) {
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

    private Genre buildGenre(ResultSet resultSet) throws SQLException {
        return new Genre((int) resultSet.getLong("genre_id"),
                resultSet.getString("genre_name"));
    }
}
