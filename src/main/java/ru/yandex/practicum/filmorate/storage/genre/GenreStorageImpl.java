package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreStorageImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(long id) {
        final String query = "SELECT * FROM genre WHERE genre_id =  ?";
        List<Genre> genres = jdbcTemplate.query(query, (rs, rowNum) -> buildGenre(rs), id);
        if (genres.isEmpty()) {
            throw new NotFoundException("Такого рейтинга не существует");
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAllGenres() {
        String query = "SELECT * FROM genre;";
        List<Genre> genres = jdbcTemplate.query(query, (rs, rowNum) -> buildGenre(rs));
        return genres;
    }

    private Genre buildGenre(ResultSet resultSet) throws SQLException {
        return new Genre((int) resultSet.getLong("genre_id"),
                resultSet.getString("genre_name"));
    }
}