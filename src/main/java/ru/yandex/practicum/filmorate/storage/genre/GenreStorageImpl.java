package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.GenreName;

import java.util.Arrays;
import java.util.List;

@Component
public class GenreStorageImpl implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GenreName> getAllGenres() {
        return Arrays.asList(GenreName.values());
    }

    @Override
    public GenreName getGenreById(int id) throws NotFoundException {
        return GenreName.values()[id - 1];
    }
}